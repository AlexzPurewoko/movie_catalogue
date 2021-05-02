package id.apwdevs.app.discover

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import id.apwdevs.app.core.domain.usecase.DiscoverPopularUseCase
import id.apwdevs.app.discover.ui.child.MovieShowViewModel
import id.apwdevs.app.libs.data.FakeDomain
import id.apwdevs.app.libs.rule.TestCoroutineRule
import id.apwdevs.app.libs.util.RecyclerTestAdapter
import id.apwdevs.app.libs.util.runTest
import id.apwdevs.app.movieshow.MainApplication
import id.apwdevs.app.res.data.MovieShowItem
import id.apwdevs.app.res.util.PageType
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class MovieShowViewModelTest {

    @get: Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @MockK
    lateinit var discoverUseCase: DiscoverPopularUseCase

    @MockK
    lateinit var mockObserver: Observer<PagingData<MovieShowItem>>

    private lateinit var captureSlot: CapturingSlot<PagingData<MovieShowItem>>

    private lateinit var viewModel: MovieShowViewModel

    private lateinit var adapter: RecyclerTestAdapter<MovieShowItem>

    @Before
    fun startup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        val app = mockk<MainApplication>(relaxed = true)
        viewModel = MovieShowViewModel(app, discoverUseCase)
        captureSlot = slot()

        adapter = RecyclerTestAdapter()
    }

    @Test
    fun `discoverPopular() movie should call method from usecase`() = runTest(testCoroutineRule) {
        every { discoverUseCase.discoverPopularMovies() } returns flow { }
        viewModel.discoverPopular(PageType.MOVIES)
        verify(exactly = 1) { discoverUseCase.discoverPopularMovies() }
    }

    @Test
    fun `discoverPopular() movie should return data when retrieve success`() = runTest(testCoroutineRule) {
        val fakeData = FakeDomain.generateListMovieDomains()
        PagingData.from(fakeData)
        val pagingData = PagingData.from(fakeData)
        val fakeFlowState = flow { emit(pagingData) }

        every { discoverUseCase.discoverPopularMovies() } returns fakeFlowState
        every { mockObserver.onChanged(capture(captureSlot)) } answers { nothing }

        val result = viewModel.discoverPopular(PageType.MOVIES)
        result.observeForever(mockObserver)

        val job = launch { adapter.submitData(captureSlot.captured) }
        delay(1000)

        Assert.assertEquals(5, adapter.itemCount)
        verify(exactly = 1) { discoverUseCase.discoverPopularMovies() }
        confirmVerified(discoverUseCase)
        job.cancel()

    }

    @Test
    fun `discoverPopular() tvshow should call method from usecase`() = runTest(testCoroutineRule) {
        every { discoverUseCase.discoverPopularTvShow() } returns flow { }
        viewModel.discoverPopular(PageType.TV_SHOW)
        verify(exactly = 1) { discoverUseCase.discoverPopularTvShow() }
    }

    @Test
    fun `discoverPopular() tvshow should return data when retrieve success`() = runTest(testCoroutineRule) {
        val fakeData = FakeDomain.generateListTvDomains()
        PagingData.from(fakeData)
        val pagingData = PagingData.from(fakeData)
        val fakeFlowState = flow { emit(pagingData) }

        every { discoverUseCase.discoverPopularTvShow() } returns fakeFlowState
        every { mockObserver.onChanged(capture(captureSlot)) } answers { nothing }

        val result = viewModel.discoverPopular(PageType.TV_SHOW)
        result.observeForever(mockObserver)

        val job = launch { adapter.submitData(captureSlot.captured) }
        delay(1000)

        Assert.assertEquals(5, adapter.itemCount)
        verify(exactly = 1) { discoverUseCase.discoverPopularTvShow() }
        confirmVerified(discoverUseCase)
        job.cancel()
    }

}