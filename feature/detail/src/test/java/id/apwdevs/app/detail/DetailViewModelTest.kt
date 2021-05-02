package id.apwdevs.app.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.usecase.DetailUseCase
import id.apwdevs.app.core.domain.usecase.FavUseCase
import id.apwdevs.app.core.utils.DataType
import id.apwdevs.app.core.utils.State
import id.apwdevs.app.detail.util.mapToItem
import id.apwdevs.app.detail.viewmodel.DetailMovieShowVM
import id.apwdevs.app.libs.data.FakeDomain
import id.apwdevs.app.libs.rule.TestCoroutineRule
import id.apwdevs.app.movieshow.MainApplication
import id.apwdevs.app.res.util.PageType
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class DetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @MockK
    lateinit var favoriteUseCase: FavUseCase

    @MockK
    lateinit var detailUseCase: DetailUseCase

    @MockK
    lateinit var favoriteObserver: Observer<State<Boolean>>

    @MockK
    lateinit var dataObserver: Observer<State<Any>>

    private lateinit var detailViewModel: DetailMovieShowVM

    @Before
    fun setup(){
        MockKAnnotations.init(this, relaxUnitFun = true)
        val applicationMock = mockk<MainApplication>(relaxed = true)
        detailViewModel = DetailMovieShowVM(applicationMock, detailUseCase, favoriteUseCase)
    }

    @Test
    fun `load detail movie should load from network only if doesn't exists in favorite`() {
        val id = 1
        val dataType = DataType.MOVIES
        val fakeData = FakeDomain.generateDetailMovieDomain(id)
        val expectedActual = DetailMovieShowVM.DataPostType(
            DetailMovieShowVM.PostType.DATA,
            fakeData.mapToItem()
        )
        coEvery { favoriteUseCase.checkIsInFavorite(id, dataType) } returns false
        every { detailUseCase.getDetailMovie(id) } returns flow {
            emit(State.Loading())
            emit(State.Success(fakeData))
        }

        detailViewModel.pageType = PageType.MOVIES
        detailViewModel.itemId = id

        detailViewModel.data.observeForever(dataObserver)
        detailViewModel.loadData()

        coVerify(exactly = 1) { favoriteUseCase.checkIsInFavorite(id, dataType) }
        verify(exactly = 1) { detailUseCase.getDetailMovie(id) }
        verify(exactly = 1) { dataObserver.onChanged(match { it is State.Loading }) }
        verify(exactly = 2) { dataObserver.onChanged(match { it is State.Success }) }
        verify(exactly = 0) { favoriteUseCase.getFavoriteMovie(id) }
        confirmVerified(dataObserver)

        assertNotNull(detailViewModel.data)
        assertTrue(detailViewModel.data.value is State.Success<*>)
        assertEquals(expectedActual, (detailViewModel.data.value as State.Success<DetailMovieShowVM.DataPostType>).data)
    }

    @Test
    fun `load detail movie should load from local only if has been saved in favorite`() {
        val id = 1
        val dataType = DataType.MOVIES
        val fakeData = FakeDomain.generateDetailMovieDomain(id)
        val expectedActual = DetailMovieShowVM.DataPostType(
                DetailMovieShowVM.PostType.DATA,
                fakeData.mapToItem()
        )
        coEvery { favoriteUseCase.checkIsInFavorite(id, dataType) } returns true
        every { favoriteUseCase.getFavoriteMovie(id) } returns flow {
            emit(State.Loading())
            emit(State.Success(fakeData))
        }

        detailViewModel.pageType = PageType.MOVIES
        detailViewModel.itemId = id

        detailViewModel.data.observeForever(dataObserver)
        detailViewModel.loadData()

        coVerify(exactly = 1) { favoriteUseCase.checkIsInFavorite(id, dataType) }
        verify(exactly = 1) { favoriteUseCase.getFavoriteMovie(id) }
        verify(exactly = 1) { dataObserver.onChanged(match { it is State.Loading }) }
        verify(exactly = 2) { dataObserver.onChanged(match { it is State.Success }) }
        verify(exactly = 0) { detailUseCase.getDetailMovie(id) }
        verify { dataObserver.onChanged(match { it is State.Success }) }

        assertNotNull(detailViewModel.data)
        assertTrue(detailViewModel.data.value is State.Success<*>)
        assertEquals(expectedActual, (detailViewModel.data.value as State.Success<DetailMovieShowVM.DataPostType>).data)
    }

    @Test
    fun `load detail movie should fail if any error when receive data from network`() {
        val id = 1
        val dataType = DataType.MOVIES
        coEvery { favoriteUseCase.checkIsInFavorite(id, dataType) } returns false
        every { detailUseCase.getDetailMovie(id) } returns flow {
            emit(State.Loading())
            emit(State.Error(Exception("Any Exception Ocurred")))
        }

        detailViewModel.pageType = PageType.MOVIES
        detailViewModel.itemId = id

        detailViewModel.data.observeForever(dataObserver)
        detailViewModel.loadData()

        coVerify(exactly = 1) { favoriteUseCase.checkIsInFavorite(id, dataType) }
        verify(exactly = 1) { detailUseCase.getDetailMovie(id) }
        verify(exactly = 1) { dataObserver.onChanged(match { it is State.Loading }) }
        verify(exactly = 1) { dataObserver.onChanged(match { it is State.Success }) }

        verify(exactly = 1) { dataObserver.onChanged(match { it is State.Error }) }
        verify(exactly = 0) { favoriteUseCase.getFavoriteMovie(id) }
        confirmVerified(dataObserver)

        assertNotNull(detailViewModel.data)
        assertTrue(detailViewModel.data.value is State.Error)
    }

    // tv show

    @Test
    fun `load detail tvshow should load from network only if doesn't exists in favorite`() {
        val id = 1
        val dataType = DataType.TVSHOW
        val fakeData = FakeDomain.generateDetailTvDomain(id)
        val expectedActual = DetailMovieShowVM.DataPostType(
                DetailMovieShowVM.PostType.DATA,
                fakeData.mapToItem()
        )
        coEvery { favoriteUseCase.checkIsInFavorite(id, dataType) } returns false
        every { detailUseCase.getDetailTvShow(id) } returns flow {
            emit(State.Loading())
            emit(State.Success(fakeData))
        }

        detailViewModel.pageType = PageType.TV_SHOW
        detailViewModel.itemId = id

        detailViewModel.data.observeForever(dataObserver)
        detailViewModel.loadData()

        coVerify(exactly = 1) { favoriteUseCase.checkIsInFavorite(id, dataType) }
        verify(exactly = 1) { detailUseCase.getDetailTvShow(id) }
        verify(exactly = 1) { dataObserver.onChanged(match { it is State.Loading }) }
        verify(exactly = 2) { dataObserver.onChanged(match { it is State.Success }) }
        verify(exactly = 0) { favoriteUseCase.getFavoriteTvShow(id) }
        confirmVerified(dataObserver)

        assertNotNull(detailViewModel.data)
        assertTrue(detailViewModel.data.value is State.Success<*>)
        assertEquals(expectedActual, (detailViewModel.data.value as State.Success<DetailMovieShowVM.DataPostType>).data)
    }

    @Test
    fun `load detail tvshow should load from local only if has been saved in favorite`() = testCoroutineRule.runBlockingTest{
        val id = 1
        val dataType = DataType.TVSHOW
        val fakeData = FakeDomain.generateDetailTvDomain(id)
        val expectedActual = DetailMovieShowVM.DataPostType(
                DetailMovieShowVM.PostType.DATA,
                fakeData.mapToItem()
        )
        coEvery { favoriteUseCase.checkIsInFavorite(id, dataType) } returns true
        every { favoriteUseCase.getFavoriteTvShow(id) } returns flow {
            emit(State.Loading())
            emit(State.Success(fakeData))
        }

        detailViewModel.pageType = PageType.TV_SHOW
        detailViewModel.itemId = id

        detailViewModel.data.observeForever(dataObserver)
        detailViewModel.loadData()

        coVerify(exactly = 1) { favoriteUseCase.checkIsInFavorite(id, dataType) }
        verify(exactly = 1) { favoriteUseCase.getFavoriteTvShow(id) }
        verify(exactly = 1) { dataObserver.onChanged(match { it is State.Loading }) }
        verify(exactly = 2) { dataObserver.onChanged(match { it is State.Success }) }
        verify(exactly = 0) { detailUseCase.getDetailTvShow(id) }
        verify { dataObserver.onChanged(match { it is State.Success }) }

        assertNotNull(detailViewModel.data)
        assertTrue(detailViewModel.data.value is State.Success<*>)
        assertEquals(expectedActual, (detailViewModel.data.value as State.Success<DetailMovieShowVM.DataPostType>).data)
    }

    @Test
    fun `load detail tvshow should fail if any error when receive data from network`() {
        val id = 1
        val dataType = DataType.TVSHOW
        coEvery { favoriteUseCase.checkIsInFavorite(id, dataType) } returns false
        every { detailUseCase.getDetailTvShow(id) } returns flow {
            emit(State.Loading())
            emit(State.Error(Exception("Any Exception Ocurred")))
        }

        detailViewModel.pageType = PageType.TV_SHOW
        detailViewModel.itemId = id

        detailViewModel.data.observeForever(dataObserver)
        detailViewModel.loadData()

        coVerify(exactly = 1) { favoriteUseCase.checkIsInFavorite(id, dataType) }
        verify(exactly = 1) { detailUseCase.getDetailTvShow(id) }
        verify(exactly = 1) { dataObserver.onChanged(match { it is State.Loading }) }
        verify(exactly = 1) { dataObserver.onChanged(match { it is State.Success }) }

        verify(exactly = 1) { dataObserver.onChanged(match { it is State.Error }) }
        verify(exactly = 0) { favoriteUseCase.getFavoriteTvShow(id) }
        confirmVerified(dataObserver)

        assertNotNull(detailViewModel.data)
        assertTrue(detailViewModel.data.value is State.Error)
    }

    @Test
    fun `toggleFavorite movie must trigger delete when has been favorited`() {
        testFavorite(true, false, PageType.MOVIES, DataType.MOVIES)
    }

    @Test
    fun `toggleFavorite movie must trigger save when hasn't been favorite`() {
        testFavorite(false, true, PageType.MOVIES, DataType.MOVIES)
    }

    @Test
    fun `toggleFavorite tvshow must trigger delete when has been favorited`() {
        testFavorite(true, false, PageType.TV_SHOW, DataType.TVSHOW)
    }

    @Test
    fun `toggleFavorite tvshow must trigger save when hasn't been favorite`() {
        testFavorite(false, true, PageType.TV_SHOW, DataType.TVSHOW)
    }

    private fun testFavorite(initialIsFavorited: Boolean, resultFavorited: Boolean, pageType: PageType, dataType: DataType) {
        val id = 1
        val captureSlot = mutableListOf<State<DetailMovieShowVM.DataPostType>>()
        val fakeData: Any = when(pageType){
            PageType.MOVIES -> FakeDomain.generateDetailMovieDomain(id)
            PageType.TV_SHOW -> FakeDomain.generateDetailTvDomain(id)
        }
        coEvery { favoriteUseCase.checkIsInFavorite(id, dataType) } returns resultFavorited
        coEvery { favoriteUseCase.unFavorite(id, dataType) } answers {nothing}
        coEvery {
            when(pageType){
                PageType.MOVIES -> favoriteUseCase.saveFavoriteMovie(fakeData as DetailMovie)
                PageType.TV_SHOW -> favoriteUseCase.saveFavoriteTvShow(fakeData as DetailTvShow)
            }
        } answers {nothing}
        every { dataObserver.onChanged(capture(captureSlot)) } just Runs

        detailViewModel.itemId = id
        detailViewModel.pageType = pageType
        detailViewModel.isFavorited = initialIsFavorited
        detailViewModel.itemData = fakeData



        detailViewModel.data.observeForever(dataObserver)
        detailViewModel.toggleFavorite()


        coVerify(exactly = 1) {
            if (initialIsFavorited)
                favoriteUseCase.unFavorite(id, dataType)
            else
                when (pageType) {
                    PageType.MOVIES -> favoriteUseCase.saveFavoriteMovie(fakeData as DetailMovie)
                    PageType.TV_SHOW -> favoriteUseCase.saveFavoriteTvShow(fakeData as DetailTvShow)
                }
        }
        coVerify(exactly = 1) { favoriteUseCase.checkIsInFavorite(id, dataType) }
        assertEquals(DetailMovieShowVM.FAVORITE_LOADING_TAG, (captureSlot[0] as State.Loading).loadingTag)
        assertEquals(
                DetailMovieShowVM.DataPostType(DetailMovieShowVM.PostType.FAVORITE_STATE, resultFavorited),
                (captureSlot[1] as State.Success).data)

        assertEquals(resultFavorited, detailViewModel.isFavorited)
    }
}