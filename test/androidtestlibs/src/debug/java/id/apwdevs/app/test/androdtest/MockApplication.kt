package id.apwdevs.app.test.androdtest

import androidx.room.Room
import com.google.gson.Gson
import id.apwdevs.app.data.source.local.room.AppDatabase
import id.apwdevs.app.data.source.remote.service.ApiService
import id.apwdevs.app.movieshow.MainApplication
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MockApplication : MainApplication() {
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