package id.apwdevs.app.core.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.cachedIn
import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.repository.MovieRepository
import id.apwdevs.app.core.repository.MovieRepoImpl
import id.apwdevs.app.core.utils.State
import id.apwdevs.app.core.utils.mapToDomain
import id.apwdevs.app.data.source.local.database.paging.PagingCaseMovieDb
import id.apwdevs.app.data.source.remote.network.MoviesNetwork
import id.apwdevs.app.libs.data.FakeDataDetail
import id.apwdevs.app.libs.rule.TestCoroutineRule
import id.apwdevs.app.libs.util.RecyclerTestAdapter
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

//@RunWith(JUnit4::class)
@RunWith(RobolectricTestRunner::class)
class MovieRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    @MockK
    lateinit var service: MoviesNetwork

    @MockK
    lateinit var pagingCaseDb: PagingCaseMovieDb

    private lateinit var movieRepository: MovieRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        movieRepository = MovieRepoImpl(service, pagingCaseDb)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun searchMovies_should_return_empty_if_data_not_found() {
        val expected = Throwable("Error 404: Resource Not Found")

        coroutineTestRule.runBlockingTest {

            coEvery { service.searchMovies("a", false) } throws expected
            coEvery { pagingCaseDb.getGenres() } returns FakeDataDetail.generateGenre()
            val adapter = RecyclerTestAdapter<Movies>()
            val result = movieRepository.searchMovies("a", false)

            // we need to define handler for exception in coroutine because submitData will return error if any error ocurred on PagingSource
            val cHandler = CoroutineExceptionHandler { _, exception ->
                Assert.assertEquals(expected.message, exception.message)
            }

            val job = launch(cHandler) {
                result.cachedIn(this).collect {
                    adapter.submitData(it)
                }
            }

            Assert.assertEquals(0, adapter.itemCount)

            coVerify {
                service.searchMovies("a", false)
                pagingCaseDb.getGenres()
            }
            confirmVerified(service)
            job.cancel()

        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun searchMovies_should_return_data_when_searching_isSuccess() {
        val movieResponse = FakeDataDetail.generateMovieResponse()
        coroutineTestRule.runBlockingTest {
            coEvery { service.searchMovies("a", false, 1) } returns movieResponse
            coEvery { service.searchMovies("a", false, 2) } returns movieResponse

            coEvery { pagingCaseDb.getGenres() } returns FakeDataDetail.generateGenre()
            val adapter = RecyclerTestAdapter<Movies>()
            val result = movieRepository.searchMovies("a", false)

            val job = launch {
                result.cachedIn(this).collect {
                    adapter.submitData(it)
                }
            }

            val actual = adapter.peek(0)
            val expectedFirstItem = movieResponse.results[0]
            Assert.assertNotNull(actual)
            Assert.assertEquals(expectedFirstItem.id, actual?.movieId)
            coVerify {
                service.searchMovies("a", false, 1)
                service.searchMovies("a", false, 2)
                pagingCaseDb.getGenres()
            }
            confirmVerified(service)
            job.cancel()
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun getDetailMovie_should_failed_when_data_not_found() {
        val movieId = 12345
        val expected = Exception("Error 404: Resource Not found")
        coroutineTestRule.runBlockingTest {
            val observer = mockk<Observer<State<DetailMovie>>>(relaxUnitFun = true)
            coEvery { service.getDetailMovies(movieId.toString()) } throws expected


            val result = movieRepository.getDetailMovie(movieId)
            var actual: Throwable? = null
            result.collect {
                observer.onChanged(it)
                if (it is State.Error) {
                    actual = it.error
                }
            }

            coVerifyOrder {
                observer.onChanged(coMatch { it is State.Loading })
                service.getDetailMovies(cmpEq(movieId.toString()))
                observer.onChanged(
                        coMatch { it is State.Error }
                )
            }
            confirmVerified(observer, service)

            Assert.assertNotNull(actual)
            Assert.assertEquals(expected, actual)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getDetailMovie_should_return_data_when_success_getData() {
        val movieId = 12345
        val fakeData = FakeDataDetail.generateMovieDetailResponse(movieId)
        val expected = fakeData.mapToDomain()
        coroutineTestRule.runBlockingTest {
            val observer = mockk<Observer<State<DetailMovie>>>(relaxUnitFun = true)
            coEvery { service.getDetailMovies(movieId.toString()) } returns fakeData

            val result = movieRepository.getDetailMovie(movieId)
            var actual: DetailMovie? = null
            result.collect {
                observer.onChanged(it)
                if (it is State.Success<DetailMovie>) {
                    actual = it.data
                }
            }

            coVerifyOrder {
                observer.onChanged(coMatch { it is State.Loading })
                service.getDetailMovies(cmpEq(movieId.toString()))
                observer.onChanged(
                        coMatch { it is State.Success<DetailMovie> }
                )
            }
            confirmVerified(observer, service)

            Assert.assertNotNull(actual)
            Assert.assertEquals(expected, actual)
        }
    }


    fun discoverPopularMovies_should_return_data_when_success_retrieving() {

    }

    fun discoverPopularMovies_should_return_empty_when_failed() {

    }
}

