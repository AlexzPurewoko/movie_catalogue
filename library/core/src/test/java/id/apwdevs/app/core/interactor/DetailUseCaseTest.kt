package id.apwdevs.app.core.interactor

import id.apwdevs.app.core.domain.repository.MovieRepository
import id.apwdevs.app.core.domain.repository.TvShowRepository
import id.apwdevs.app.core.domain.usecase.DetailInteractor
import id.apwdevs.app.core.domain.usecase.DetailUseCase
import id.apwdevs.app.core.utils.State
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

class DetailUseCaseTest {
    @MockK
    lateinit var movieRepository: MovieRepository

    @MockK
    lateinit var tvShowRepository: TvShowRepository

    lateinit var detailUseCase: DetailUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        detailUseCase = DetailInteractor(movieRepository, tvShowRepository)
    }

    @Test
    fun `getDetailMovie() must call repository and return right value`() {
        runBlocking {
            val movieId = 1
            val fakeData = FakeDomain.generateDetailMovieDomain(movieId)
            val expected = flow {
                emit(State.Success(fakeData))
            }

            every { movieRepository.getDetailMovie(any()) } returns expected

            val actual = detailUseCase.getDetailMovie(movieId)

            Assert.assertNotNull(actual)
            Assert.assertEquals(expected, actual)

            verify(exactly = 1) { movieRepository.getDetailMovie(cmpEq(movieId)) }

        }
    }

    @Test
    fun `getDetailTvShow() must call repository and return right value`() {
        runBlocking {
            val tvId = 1
            val fakeData = FakeDomain.generateDetailTvDomain(1)
            val expected = flow {
                emit(State.Success(fakeData))
            }

            every { tvShowRepository.getDetailTvShow(any()) } returns expected

            val actual = detailUseCase.getDetailTvShow(tvId)

            Assert.assertNotNull(actual)
            Assert.assertEquals(expected, actual)


            verify(exactly = 1) { tvShowRepository.getDetailTvShow(cmpEq(tvId)) }

        }
    }
}