package id.apwdevs.app.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.model.TvShow
import id.apwdevs.app.core.domain.usecase.SearchUseCase
import id.apwdevs.app.libs.data.FakeDomain
import id.apwdevs.app.libs.rule.TestCoroutineRule
import id.apwdevs.app.libs.util.RecyclerTestAdapter
import id.apwdevs.app.res.util.PageType
import id.apwdevs.app.search.model.SearchItem
import id.apwdevs.app.search.ui.SearchVewModel
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.robolectric.RobolectricTestRunner
import ru.ldralighieri.corbind.internal.asInitialValueFlow
import ru.ldralighieri.corbind.internal.offerCatching

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class SearchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @MockK
    lateinit var searchUseCase: SearchUseCase

    @Captor
    private lateinit var liveDataCaptor: ArgumentCaptor<PagingData<SearchItem>>

    private lateinit var searchViewModel: SearchVewModel

    private lateinit var adapter: RecyclerTestAdapter<SearchItem>

    @Before
    fun startup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        searchViewModel = SearchVewModel(searchUseCase)
        adapter = RecyclerTestAdapter()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `search() should select the right page`() {
        runBlocking {
            val query = "a"
            val moviePagingData = flow {
                emit(PagingData.from(FakeDomain.generateListMovieDomains()))
            }
            val tvPagingData = flow {
                emit(PagingData.from(FakeDomain.generateListTvDomains()))
            }
            val composeSearchDataMovie = SearchVewModel.SearchData(query, PageType.MOVIES, false)
            val composeSearchDataTvShow = SearchVewModel.SearchData(query, PageType.TV_SHOW, false)


            every { searchUseCase.searchMovie(query, false) } returns moviePagingData
            every { searchUseCase.searchTvShow(query, false) } returns tvPagingData
            searchViewModel.search(composeSearchDataMovie)

            verify(exactly = 1) { searchUseCase.searchMovie("a", false) }

            searchViewModel.search(composeSearchDataTvShow)

            verify(exactly = 1) { searchUseCase.searchTvShow("a", false) }
            confirmVerified(searchUseCase)
        }
    }

    @Test
    fun `search(movies) should return right data when success`() {
        testCoroutineRule.runBlockingTest {
            val query = "a"
            val fakePaging = PagingData.from(FakeDomain.generateListMovieDomains())
            val moviePagingData = flow {
                emit(fakePaging)
            }
            val composeSearchData = SearchVewModel.SearchData(query, PageType.MOVIES, false)


            val captureSlot = slot<PagingData<SearchItem>>()
            val observer = mockk<Observer<PagingData<SearchItem>>>(relaxUnitFun = true)
            every { searchUseCase.searchMovie(query, false) } returns moviePagingData
            every { observer.onChanged(capture(captureSlot)) } answers { nothing }

            val liveData = searchViewModel.search(composeSearchData)
            liveData.observeForever(observer)

            val job = launch {
                adapter.submitData(captureSlot.captured)
            }
            delay(1000)

            Assert.assertEquals(5, adapter.itemCount)
            verify(atLeast = 1) { searchUseCase.searchMovie(query, false) }
            job.cancel()
        }
    }

    @Test
    fun `search(movies) should not perform search again if parameters still same`() {
        testCoroutineRule.runBlockingTest {
            val query = "a"
            val fakePaging = PagingData.from(FakeDomain.generateListMovieDomains())
            val moviePagingData = flow {
                emit(fakePaging)
            }
            val composeSearchData = SearchVewModel.SearchData(query, PageType.MOVIES, false)

            val captureSlot = slot<PagingData<SearchItem>>()
            val observer = mockk<Observer<PagingData<SearchItem>>>(relaxUnitFun = true)

            every { searchUseCase.searchMovie(query, false) } returns moviePagingData
            every { observer.onChanged(capture(captureSlot)) } answers { nothing }

            searchViewModel.search(composeSearchData).observeForever(observer)
            delay(500)

            searchViewModel.search(composeSearchData).observeForever(observer)
            delay(500)

            val job = launch {
                adapter.submitData(captureSlot.captured)
            }

            Assert.assertEquals(5, adapter.itemCount)
            verify(exactly = 1) { searchUseCase.searchMovie(query, false) }
            job.cancel()
        }
    }

    @Test
    fun `search(tvshow) should return right data when success`() {
        testCoroutineRule.runBlockingTest {
            val query = "a"
            val fakePaging = PagingData.from(FakeDomain.generateListTvDomains())
            val tvPagingData = flow {
                emit(fakePaging)
            }
            val composeSearchData = SearchVewModel.SearchData(query, PageType.TV_SHOW, false)

            val captureSlot = slot<PagingData<SearchItem>>()
            val observer = mockk<Observer<PagingData<SearchItem>>>(relaxUnitFun = true)

            every { searchUseCase.searchTvShow(query, false) } returns tvPagingData
            every { observer.onChanged(capture(captureSlot)) } answers { nothing }

            val liveData = searchViewModel.search(composeSearchData)
            liveData.observeForever(observer)

            val job = launch {
                adapter.submitData(captureSlot.captured)
            }
            delay(1000)

            Assert.assertEquals(5, adapter.itemCount)
            verify(atLeast = 1) { searchUseCase.searchTvShow(query, false) }
            job.cancel()
        }
    }

    @Test
    fun `search(tvshow) should not perform search again if parameters still same`() {
        testCoroutineRule.runBlockingTest {
            val query = "a"
            val fakePaging = PagingData.from(FakeDomain.generateListTvDomains())
            val tvPagingData = flow {
                emit(fakePaging)
            }
            val composeSearchData = SearchVewModel.SearchData(query, PageType.TV_SHOW, false)

            val captureSlot = slot<PagingData<SearchItem>>()
            val observer = mockk<Observer<PagingData<SearchItem>>>(relaxUnitFun = true)

            every { searchUseCase.searchTvShow(query, false) } returns tvPagingData
            every { observer.onChanged(capture(captureSlot)) } answers { nothing }

            searchViewModel.search(composeSearchData).observeForever(observer)
            delay(500)

            searchViewModel.search(composeSearchData).observeForever(observer)
            delay(500)

            val job = launch {
                adapter.submitData(captureSlot.captured)
            }

            Assert.assertEquals(5, adapter.itemCount)
            verify(exactly = 1) { searchUseCase.searchTvShow(query, false) }
            job.cancel()
        }
    }

    @Test
    fun `initViewInteractions() should notify livedata when any changes occured`() {
        testCoroutineRule.runBlockingTest {
            var callTextChanges: ((CharSequence) -> Boolean)? = null

            val flowTextChanges = channelFlow<CharSequence> {
                callTextChanges = this::offerCatching
            }

            val textChanges = flowTextChanges.asInitialValueFlow("")

            var callCheckedChanges: (Boolean) -> Boolean = { false }
            val flowCheckedChanges = channelFlow<Boolean> {
                callCheckedChanges = this::offerCatching
            }
            val checkedChanges = flowCheckedChanges.asInitialValueFlow(false)

            var callSpinnerChanges: (Int) -> Boolean = { false }
            val flowSpinnerChanges = channelFlow<Int> {
                callSpinnerChanges = this::offerCatching
            }
            val spinnerChanges = flowSpinnerChanges.asInitialValueFlow(-1)

            val captor = slot<Boolean>()
            val observer = mockk<Observer<Boolean>>()

            every { observer.onChanged(capture(captor)) } answers { nothing }
            searchViewModel.initViewInteractions(textChanges, checkedChanges, spinnerChanges).observeForever(observer)

            Assert.assertTrue(captor.isCaptured)

            verify(exactly = 1) { observer.onChanged(true) }
            callTextChanges?.invoke("a")

            verify(exactly = 1) { observer.onChanged(true) }

            callCheckedChanges.invoke(true)
            verify(exactly = 1) { observer.onChanged(true) }

            callSpinnerChanges.invoke(10)
            verify(exactly = 1) { observer.onChanged(true) }

            confirmVerified(observer)
        }
    }

}

object SearchHelper {
    fun mapMoviesDomainToSearchItem(movies: Movies): SearchItem {
        return SearchItem(
                id = movies.movieId, title = movies.title, backdropImage = movies.backdropPath,
                voteAverage = movies.voteAverage, releaseDate = movies.releaseDate
        )
    }

    fun mapTvShowDomainToSearchItem(tvShow: TvShow): SearchItem {
        return SearchItem(
                id = tvShow.tvId, title = tvShow.name, backdropImage = tvShow.backdropPath,
                voteAverage = tvShow.voteAverage, releaseDate = tvShow.firstAirDate
        )
    }
}
