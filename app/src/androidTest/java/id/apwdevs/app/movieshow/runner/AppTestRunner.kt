package id.apwdevs.app.movieshow.runner

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.test.runner.AndroidJUnitRunner
import com.google.gson.Gson
import id.apwdevs.app.data.source.local.room.AppDatabase
import id.apwdevs.app.data.source.remote.service.ApiService
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import id.apwdevs.app.movieshow.MainApplication

@Suppress("unused")
class AppTestRunner: AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, MockApplication::class.java.name, context)
    }
}

class MockApplication: MainApplication() {
    override val netModules: Module
        get() = module {
            single {
                Retrofit.Builder().apply {
                    baseUrl("http://localhost:8080/")
                    addConverterFactory(GsonConverterFactory.create(Gson()))
                }.build().create(ApiService::class.java)
            }
        }

    override val dbModules: Module
        get() = module {
            single {
                Room.inMemoryDatabaseBuilder(get(), AppDatabase::class.java).build()
            }
        }
}