package id.apwdevs.app.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.apwdevs.app.core.domain.usecase.SearchUseCase
import id.apwdevs.app.search.ui.SearchVewModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
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
