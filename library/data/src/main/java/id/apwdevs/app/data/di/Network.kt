package id.apwdevs.app.data.di

import com.google.gson.Gson
import id.apwdevs.app.data.source.remote.network.MoviesNetwork
import id.apwdevs.app.data.source.remote.network.MoviesNetworkImpl
import id.apwdevs.app.data.source.remote.network.TvShowsNetwork
import id.apwdevs.app.data.source.remote.network.TvShowsNetworkImpl
import id.apwdevs.app.data.source.remote.service.ApiService
import id.apwdevs.app.data.utils.Config
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

    factory<Converter.Factory> {
        GsonConverterFactory.create(Gson())
    }

    single {
        OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            readTimeout(10, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)
            callTimeout(10, TimeUnit.SECONDS)
        }.build()
    }

    single {
        Retrofit.Builder().apply {
            client(get())
            baseUrl(Config.BASE_URL)
            addConverterFactory(get())
        }.build().create(ApiService::class.java)
    }
}

val netWorkAccessModule = module {
    factory<MoviesNetwork> { MoviesNetworkImpl(get()) }
    factory<TvShowsNetwork> { TvShowsNetworkImpl(get()) }
}