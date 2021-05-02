package id.apwdevs.app.core.repo

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.cachedIn
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.TvShow
import id.apwdevs.app.core.domain.repository.TvShowRepository
import id.apwdevs.app.core.repository.TvShowRepoImpl
import id.apwdevs.app.core.utils.State
import id.apwdevs.app.core.utils.mapToDomain
import id.apwdevs.app.data.source.local.database.paging.PagingCaseTvShowDb
import id.apwdevs.app.data.source.local.database.paging.PagingTvShowCaseDbInteractor
import id.apwdevs.app.data.source.local.room.AppDatabase
import id.apwdevs.app.data.source.remote.network.TvShowsNetwork
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

@RunWith(RobolectricTestRunner::class)
class TvShowRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    @MockK
    lateinit var service: TvShowsNetwork

    lateinit var pagingCaseDb: PagingCaseTvShowDb

    lateinit var appDatabase: AppDatabase

    private val context: Context by lazy {
        ApplicationProvider.getApplicationContext()
    }


    private lateinit var tvShowRepository: TvShowRepository

    @Before
    fun setup() {
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries().build()
        pagingCaseDb = spyk(PagingTvShowCaseDbInteractor(appDatabase))
        MockKAnnotations.init(this)
        tvShowRepository = TvShowRepoImpl(service, pagingCaseDb)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun searchMovies_should_return_empty_if_data_not_found() {
        val expected = Throwable("Error 404: Resource Not Found")

        coroutineTestRule.runBlockingTest {

            coEvery { service.searchTvShows("a", false) } throws expected
            coEvery { pagingCaseDb.getGenres() } returns FakeDataDetail.generateGenre()
            val adapter = RecyclerTestAdapter<TvShow>()
            val result = tvShowRepository.searchTvShow("a", false)

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
                service.searchTvShows("a", false)
                pagingCaseDb.getGenres()
            }
            confirmVerified(service)
            job.cancel()

        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun searchMovies_should_return_data_when_searching_isSuccess() {
        val tvShowResponse = FakeDataDetail.generateTvResponse("a")
        coroutineTestRule.runBlockingTest {
            coEvery { service.searchTvShows("a", false, 1) } returns tvShowResponse
            coEvery { service.searchTvShows("a", false, 2) } returns tvShowResponse
            coEvery { pagingCaseDb.getGenres() } returns FakeDataDetail.generateGenre()
            val adapter = RecyclerTestAdapter<TvShow>()
            val result = tvShowRepository.searchTvShow("a", false)

            val job = launch {
                result.cachedIn(this).collect {
                    adapter.submitData(it)
                }
            }

            val actual = adapter.peek(0)
            val expectedFirstItem = tvShowResponse.results[0]

            Assert.assertNotNull(actual)
            Assert.assertEquals(expectedFirstItem.id, actual?.tvId)

            coVerify {
                service.searchTvShows("a", false, 1)
                service.searchTvShows("a", false, 2)
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
            val observer = mockk<Observer<State<DetailTvShow>>>(relaxUnitFun = true)
            coEvery { service.getDetailTvShows(movieId.toString()) } throws expected


            val result = tvShowRepository.getDetailTvShow(movieId)
            var actual: Throwable? = null
            result.collect {
                observer.onChanged(it)
                if (it is State.Error) {
                    actual = it.error
                }
            }

            coVerifyOrder {
                observer.onChanged(coMatch { it is State.Loading })
                service.getDetailTvShows(cmpEq(movieId.toString()))
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
        val tvId = 12345
        val fakeData = FakeDataDetail.generateTvDetailResponse(tvId)
        val expected = fakeData.mapToDomain()
        coroutineTestRule.runBlockingTest {
            val observer = mockk<Observer<State<DetailTvShow>>>(relaxUnitFun = true)
            coEvery { service.getDetailTvShows(tvId.toString()) } returns fakeData

            val result = tvShowRepository.getDetailTvShow(tvId)
            var actual: DetailTvShow? = null
            result.collect {
                observer.onChanged(it)
                if (it is State.Success) {
                    actual = it.data
                }
            }

            coVerifyOrder {
                observer.onChanged(coMatch { it is State.Loading })
                service.getDetailTvShows(cmpEq(tvId.toString()))
                observer.onChanged(
                        coMatch { it is State.Success<DetailTvShow> }
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