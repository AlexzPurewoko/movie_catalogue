package id.apwdevs.app.movieshow

import android.app.Application
import androidx.annotation.VisibleForTesting
import id.apwdevs.app.core.di.repoModule
import id.apwdevs.app.core.di.useCaseModule
import id.apwdevs.app.data.di.databaseModule
import id.apwdevs.app.data.di.dbAccessModule
import id.apwdevs.app.data.di.networkModule
import id.apwdevs.app.movieshow.util.IdlingResourceHelper
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@Suppress("unused")
open class MainApplication: Application() {

    val idlingRes: IdlingResourceHelper by lazy { provideIdling() }

    // provide for testing
    @VisibleForTesting
    open val netModules = networkModule

    @VisibleForTesting
    open val dbModules = databaseModule

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            androidLogger(Level.DEBUG)
            modules(
                    dbModules,
                    dbAccessModule,
                    netModules,
                    repoModule,
                    useCaseModule,
            )
        }
    }


    private fun provideIdling(): IdlingResourceHelper {
        return if(BuildConfig.DEBUG)
                Class.forName("id.apwdevs.app.movieshow.DebugUtilKt")
                    .getDeclaredMethod("getProvideIdling")
                    .invoke(null) as IdlingResourceHelper
            else IdlingResourceHelper()
    }
}