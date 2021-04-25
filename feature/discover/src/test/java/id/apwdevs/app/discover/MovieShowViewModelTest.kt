package id.apwdevs.app.discover

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import id.apwdevs.app.core.domain.usecase.DiscoverPopularUseCase
import id.apwdevs.app.core.utils.State
import id.apwdevs.app.discover.ui.child.MovieShowViewModel
import id.apwdevs.app.libs.data.FakeDomain
import id.apwdevs.app.libs.rule.TestCoroutineRule
import id.apwdevs.app.libs.util.runTest
import id.apwdevs.app.movieshow.MainApplication
import id.apwdevs.app.res.data.MovieShowItem
import id.apwdevs.app.res.util.PageType
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
    lateinit var mockObserver: Observer<State<List<MovieShowItem>>>

    private val application = MainApplication()

    private lateinit var viewModel: MovieShowViewModel

    @Before
    fun startup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = MovieShowViewModel(discoverUseCase)
    }

    @Test
    fun `discoverPopular() movie should call method from usecase`() = runTest(testCoroutineRule) {
        every { discoverUseCase.discoverPopularMovies() } returns flow { }
        viewModel.discoverPopular(PageType.MOVIES)
        verify(exactly = 1) { discoverUseCase.discoverPopularMovies() }
    }

    @Test
    fun `discoverPopular() movie should return data when retrieve success`() = runTest(testCoroutineRule) {

    }

    @Test
    fun `discoverPopular() movie should throw error when failed to retrieve data`() = runTest(testCoroutineRule) {

    }

    @Test
    fun `discoverPopular() tvshow should call method from usecase`() = runTest(testCoroutineRule) {
        every { discoverUseCase.discoverPopularTvShow() } returns flow { }
        viewModel.discoverPopular(PageType.TV_SHOW)
        verify(exactly = 1) { discoverUseCase.discoverPopularTvShow() }
    }

    @Test
    fun `discoverPopular() tvshow should return data when retrieve success`() = runTest(testCoroutineRule) {

    }

    @Test
    fun `discoverPopular() tvshow should throw error when failed to retrieve data`() = runTest(testCoroutineRule) {

    }
}