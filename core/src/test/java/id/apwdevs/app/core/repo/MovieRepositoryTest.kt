package id.apwdevs.app.core.repo

import id.apwdevs.app.core.domain.repository.MoviesRepository
import id.apwdevs.app.core.repository.MovieRepoImpl
import id.apwdevs.app.data.source.remote.service.ApiService
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class MovieRepositoryTest {

    private val service: ApiService by lazy {
        Retrofit.Builder()
                .baseUrl("http://localhost:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiService::class.java)
    }


    private lateinit var movieRepository: MoviesRepository

    @Before
    fun setup() {
        movieRepository = MovieRepoImpl()
    }

    fun searchMovies_should_return_empty_if_data_not_found() {

    }

    fun searchMovies_should_return_data_when_searching_isSuccess() {

    }

    fun getDetailMovie_should_return_empty_if_data_not_found() {

    }

    fun getDetailMovie_should_return_data_when_success_getData() {

    }

    fun discoverPopularMovies_should_return_data_when_success_retrieving() {

    }

    fun discoverPopularMovies_should_return_empty_when_not_found() {

    }
}