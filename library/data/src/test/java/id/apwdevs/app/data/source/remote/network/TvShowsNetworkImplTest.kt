package id.apwdevs.app.data.source.remote.network

import id.apwdevs.app.data.source.remote.service.ApiService
import id.apwdevs.app.data.utils.Config
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class TvShowsNetworkImplTest {
    @MockK
    lateinit var apiService: ApiService

    private lateinit var tvShowsNetworkImpl: TvShowsNetworkImpl

    private val movieId: String = "1"

    private val token: String = Config.TOKEN

    private val language: String = "en-US"

    private val query = "a"

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        tvShowsNetworkImpl = TvShowsNetworkImpl(apiService)
    }

    @Test
    fun `should call getPopularTvShows from api source`() {
        runBlocking {
            tvShowsNetworkImpl.getPopularTvShows(1)
            coVerify(exactly = 1) { apiService.getPopularTvShows(token, language, 1) }
        }
    }

    @Test
    fun `should call getDetailTvShow from api source`() {
        runBlocking {
            tvShowsNetworkImpl.getDetailTvShows(movieId)
            coVerify(exactly = 1) { apiService.getDetailTvShows(movieId, token, language) }
        }
    }

    @Test
    fun `should call searchTvShow from api source`() {
        runBlocking {
            tvShowsNetworkImpl.searchTvShows(query, true)
            coVerify(exactly = 1) { apiService.searchTvShow(token, 1, query, true) }
        }
    }

    @Test
    fun `should call getTvShowsGenre from api source`() {
        runBlocking {
            tvShowsNetworkImpl.getTvShowGenre()
            coVerify(exactly = 1) { apiService.getTvShowGenre(token) }
        }
    }
}