package id.apwdevs.app.data.source.remote

import com.google.gson.Gson
import id.apwdevs.app.data.BuildConfig
import id.apwdevs.app.data.source.remote.service.ApiService
import id.apwdevs.app.data.utils.Config
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RemoteInit {
    
    private val serverHostname = "api.themoviedb.org"
    
    private fun buildCertificatePinner() =
        CertificatePinner.Builder()
            .add(serverHostname, "sha256/+vqZVAzTqUP8BGkfl88yU7SQ3C8J2uNEa55B7RZjEg0=")
            .add(serverHostname, "sha256/JSMzqOOrtyOT1kmau6zKhgT676hGgczD5VMdRMyJZFA=")
            .add(serverHostname, "sha256/++MBgDH5WGvL9Bcn5Be30cRcL0f5O+NyoXuWtQdX1aI=")
            .add(serverHostname, "sha256/KwccWaCgrnaw6tsrrSO61FgLacNgG2MMLq8GE6+oP5I=")
            .build()

    private fun buildOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().apply {

            if(BuildConfig.DEBUG)
                addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                )
            certificatePinner(buildCertificatePinner())
            readTimeout(10, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)
            callTimeout(10, TimeUnit.SECONDS)
        }.build()

    fun init(): ApiService {
        return Retrofit.Builder().apply {
            client(buildOkHttpClient())
            baseUrl(Config.BASE_URL)
            addConverterFactory(GsonConverterFactory.create(Gson()))
        }.build().create(ApiService::class.java)
    }
}