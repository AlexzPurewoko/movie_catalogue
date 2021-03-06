package id.apwdevs.app.core.interactor

import androidx.paging.PagingData
import id.apwdevs.app.core.domain.repository.MovieRepository
import id.apwdevs.app.core.domain.repository.TvShowRepository
import id.apwdevs.app.core.domain.usecase.DiscoverPopularInteractor
import id.apwdevs.app.core.domain.usecase.DiscoverPopularUseCase
import id.apwdevs.app.libs.data.FakeDomain
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class DiscoverPopularUseCaseTest {

    @MockK
    lateinit var movieRepository: MovieRepository

    @MockK
    lateinit var tvShowRepository: TvShowRepository

    private lateinit var popularUseCase: DiscoverPopularUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        popularUseCase = DiscoverPopularInteractor(movieRepository, tvShowRepository)
    }

    @Test
    fun `discoverPopularMovies() must call repository and return right value`() {
        runBlocking {
            val fakeData = FakeDomain.generateListMovieDomains()
            val expected = flow {
                emit(PagingData.from(fakeData))
            }

            every { movieRepository.discoverPopularMovies() } returns expected

            val actual = popularUseCase.discoverPopularMovies()

            Assert.assertNotNull(actual)
            Assert.assertEquals(expected, actual)

            verify(exactly = 1) { movieRepository.discoverPopularMovies() }

        }
    }

    @Test
    fun `discoverPopularTvShow() must call repository and return right value`() {
        runBlocking {
            val fakeData = FakeDomain.generateListTvDomains()
            val expected = flow {
                emit(PagingData.from(fakeData))
            }

            every { tvShowRepository.discoverPopularTvShow() } returns expected

            val actual = popularUseCase.discoverPopularTvShow()

            Assert.assertNotNull(actual)
            Assert.assertEquals(expected, actual)

            verify(exactly = 1) { tvShowRepository.discoverPopularTvShow() }

        }
    }
}