package id.apwdevs.app.data.source.remote.network

import id.apwdevs.app.data.source.remote.service.ApiService
import id.apwdevs.app.data.utils.Config
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MoviesNetworkImplTest {

    @MockK
    lateinit var apiService: ApiService

    private lateinit var movieNetwork: MoviesNetworkImpl

    private val movieId: String = "1"

    private val token: String = Config.TOKEN

    private val language: String = "en-US"

    private val query = "a"

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        movieNetwork = MoviesNetworkImpl(apiService)
    }

    @Test fun `should call getPopularMovies from api source`(){
        runBlocking {
            movieNetwork.getPopularMovies(1)
            coVerify(exactly = 1) { apiService.getPopularMovies(token, language, 1) }
        }
    }

    @Test fun `should call getDetailMovies from api source`(){
        runBlocking {
            movieNetwork.getDetailMovies(movieId)
            coVerify(exactly = 1) { apiService.getDetailMovies(movieId, token, language) }
        }
    }

    @Test fun `should call searchMovies from api source`(){
        runBlocking {
            movieNetwork.searchMovies(query, true)
            coVerify(exactly = 1) { apiService.searchMovies(token, 1, query, true)}
        }
    }

    @Test fun `should call getMovieGenre from api source`(){
        runBlocking {
            movieNetwork.getMovieGenre()
            coVerify(exactly = 1) { apiService.getMovieGenre(token) }
        }
    }
}