package id.apwdevs.app.core.interactor

import androidx.paging.PagingData
import id.apwdevs.app.core.domain.repository.MovieRepository
import id.apwdevs.app.core.domain.repository.TvShowRepository
import id.apwdevs.app.core.domain.usecase.SearchInteractor
import id.apwdevs.app.core.domain.usecase.SearchUseCase
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


class SearchUseCaseTest {

    @MockK
    lateinit var movieRepository: MovieRepository

    @MockK
    lateinit var tvShowRepository: TvShowRepository

    lateinit var searchUseCase: SearchUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        searchUseCase = SearchInteractor(movieRepository, tvShowRepository)
    }

    @Test
    fun `searchMovie() must call repository and return right value`() {
        runBlocking {
            val fakeData = FakeDomain.generateListMovieDomains()
            val expected = flow {
                emit(PagingData.from(fakeData))
            }

            every { movieRepository.searchMovies(any(), any()) } returns expected

            val actual = searchUseCase.searchMovie("a", false)

            Assert.assertNotNull(actual)
            Assert.assertEquals(expected, actual)

            verify(exactly = 1) { movieRepository.searchMovies(cmpEq("a"), cmpEq(false)) }

        }
    }

    @Test
    fun `searchTvShow() must call repository and return right value`() {
        runBlocking {
            val fakeData = FakeDomain.generateListTvDomains()
            val expected = flow {
                emit(PagingData.from(fakeData))
            }

            every { tvShowRepository.searchTvShow(any(), any()) } returns expected

            val actual = searchUseCase.searchTvShow("a", false)

            Assert.assertNotNull(actual)
            Assert.assertEquals(expected, actual)

            verify(exactly = 1) { tvShowRepository.searchTvShow(cmpEq("a"), cmpEq(false)) }

        }
    }
}