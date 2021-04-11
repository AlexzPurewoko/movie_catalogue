package id.apwdevs.app.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import id.apwdevs.app.core.data.FakeDomain
import id.apwdevs.app.core.domain.usecase.SearchUseCase
import id.apwdevs.app.res.util.PageType
import id.apwdevs.app.search.ui.SearchVewModel
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SearchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var searchUseCase: SearchUseCase

    private lateinit var searchViewModel: SearchVewModel

    @Before
    fun startup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        searchViewModel = SearchVewModel(searchUseCase)
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
            every { searchUseCase.searchMovie(query, false) } returns moviePagingData
            every { searchUseCase.searchTvShow(query, false) } returns tvPagingData
            searchViewModel.search("a", pageType = PageType.MOVIES, includeAdult = false)

            verify(exactly = 1) { searchUseCase.searchMovie("a", false) }

            searchViewModel.search("a", pageType = PageType.TV_SHOW, includeAdult = false)

            verify(exactly = 1) { searchUseCase.searchTvShow("a", false) }
            confirmVerified(searchUseCase)
        }
    }

    @Test
    fun `search(movies) should return right data when success`() {

    }

    @Test
    fun `search(movies) should not perform search again if parameters still same`() {

    }

    @Test
    fun `search(tvshow) should return right data when success`() {

    }

    @Test
    fun `search(tvshow) should not perform search again if parameters still same`() {

    }

    @Test
    fun `initViewInteractions() should notify livedata when any changes occured`() {

    }

}
