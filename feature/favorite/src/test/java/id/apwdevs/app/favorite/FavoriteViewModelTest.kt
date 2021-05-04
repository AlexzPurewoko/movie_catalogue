package id.apwdevs.app.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import id.apwdevs.app.core.domain.usecase.FavUseCase
import id.apwdevs.app.core.utils.State
import id.apwdevs.app.favorite.viewmodel.FavoriteViewModel
import id.apwdevs.app.libs.data.FakeDomain
import id.apwdevs.app.libs.rule.TestCoroutineRule
import id.apwdevs.app.libs.util.runTest
import id.apwdevs.app.movieshow.MainApplication
import id.apwdevs.app.res.data.MovieShowItem
import id.apwdevs.app.res.util.mapToItem
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class FavoriteViewModelTest {

    @get: Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @MockK
    lateinit var favoriteUseCase: FavUseCase

    @MockK
    lateinit var mockObserver: Observer<State<List<MovieShowItem>>>

    private val application = MainApplication()

    private lateinit var viewModel: FavoriteViewModel

    @Before
    fun startup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = FavoriteViewModel(application, favoriteUseCase)
    }

    @After
    fun release() {

    }

    @Test
    fun `getFavoriteMovies() should call method from usecase`() = runTest(testCoroutineRule) {
        every { favoriteUseCase.getAllFavoriteMovies() } returns flow { }
        viewModel.getFavoriteMovies()
        verify(exactly = 1) { favoriteUseCase.getAllFavoriteMovies() }
    }

    @Test
    fun `getAllFavoriteMovies() should return data when retrieve success`() =
        runTest(testCoroutineRule) {
            val fakeData = FakeDomain.generateListMovieDomains()
            val state = State.Success(fakeData)
            val mappedState = state.copyTo {
                it.map { movie -> movie.mapToItem() }
            }
            val fakeFlowState = flow {
                emit(state)
            }
            every { favoriteUseCase.getAllFavoriteMovies() } returns fakeFlowState
            val result = viewModel.getFavoriteMovies()
            var resultLiveData: State<List<MovieShowItem>>? = null
            result.observeForever {
                mockObserver.onChanged(it)
                resultLiveData = it
            }

            verify(exactly = 1) { mockObserver.onChanged(match { it is State.Success && it.data == mappedState.data }) }

            Assert.assertNotNull(resultLiveData)
            Assert.assertEquals(mappedState.data, resultLiveData?.data)
        }

    @Test
    fun `getAllFavoriteMovies() should throw error when failed to retrieve data`() =
        runTest(testCoroutineRule) {

            val throwable = Exception("Error")
            val fakeFlowState = flow {
                emit(State.Error(throwable))
            }

            every { favoriteUseCase.getAllFavoriteMovies() } returns fakeFlowState

            var resultLiveData: State<List<MovieShowItem>>? = null
            viewModel.getFavoriteMovies().observeForever {
                mockObserver.onChanged(it)
                resultLiveData = it
            }

            verify(exactly = 1) {
                mockObserver.onChanged(match {
                    it is State.Error
                })
            }

            Assert.assertNotNull(resultLiveData)
            Assert.assertEquals(throwable, resultLiveData?.error)
        }

    @Test
    fun `getFavoriteTvShows() should call method from usecase`() = runTest(testCoroutineRule) {
        every { favoriteUseCase.getAllFavoriteTvShows() } returns flow { }
        viewModel.getFavoriteTvShows()
        verify(exactly = 1) { favoriteUseCase.getAllFavoriteTvShows() }
    }

    @Test
    fun `getAllFavoriteTvShows() should return data when retrieve success`() =
        runTest(testCoroutineRule) {
            val fakeData = FakeDomain.generateListTvDomains()
            val state = State.Success(fakeData)
            val mappedState = state.copyTo {
                it.map { tvShow -> tvShow.mapToItem() }
            }
            val fakeFlowState = flow {
                emit(state)
            }
            every { favoriteUseCase.getAllFavoriteTvShows() } returns fakeFlowState

            val result = viewModel.getFavoriteTvShows()
            var resultLiveData: State<List<MovieShowItem>>? = null
            result.observeForever {
                mockObserver.onChanged(it)
                resultLiveData = it
            }

            verify(exactly = 1) { mockObserver.onChanged(match { it is State.Success && it.data == mappedState.data }) }

            Assert.assertNotNull(resultLiveData)
            Assert.assertEquals(mappedState.data, resultLiveData?.data)
        }

    @Test
    fun `getAllFavoriteTvShows() should throw error when failed to retrieve data`() =
        runTest(testCoroutineRule) {

            val throwable = Exception("Error")
            val fakeFlowState = flow {
                emit(State.Error(throwable))
            }

            every { favoriteUseCase.getAllFavoriteTvShows() } returns fakeFlowState

            var resultLiveData: State<List<MovieShowItem>>? = null
            viewModel.getFavoriteTvShows().observeForever {
                mockObserver.onChanged(it)
                resultLiveData = it
            }

            verify(exactly = 1) {
                mockObserver.onChanged(match {
                    it is State.Error
                })
            }

            Assert.assertNotNull(resultLiveData)
            Assert.assertEquals(throwable, resultLiveData?.error)
        }

}