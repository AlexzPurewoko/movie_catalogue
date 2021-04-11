package id.apwdevs.app.data.di

import androidx.room.Room
import com.google.gson.Gson
import id.apwdevs.app.data.source.local.room.AppDatabase
import id.apwdevs.app.data.source.remote.service.ApiService
import id.apwdevs.app.data.utils.Config
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(), AppDatabase::class.java, "app_database.db"
        ).build()
    }
}

val networkModule = module {

    factory <Converter.Factory> {
        GsonConverterFactory.create(Gson())
    }

    single { OkHttpClient.Builder().apply {
        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        readTimeout(10, TimeUnit.SECONDS)
        writeTimeout(10, TimeUnit.SECONDS)
        callTimeout(10, TimeUnit.SECONDS)
    }.build() }

    single {
        Retrofit.Builder().apply {
            client(get())
            baseUrl(Config.BASE_URL)
            addConverterFactory(get())
        }.build().create(ApiService::class.java)
    }
}