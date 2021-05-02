package id.apwdevs.app.data.paging

import android.content.Context
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import id.apwdevs.app.data.paging.dispatcher.SearchMoviePagingDispatcher
import id.apwdevs.app.data.source.remote.network.MoviesNetwork
import id.apwdevs.app.data.source.remote.paging.SearchMoviePagingSource
import id.apwdevs.app.data.source.remote.response.MovieItemResponse
import id.apwdevs.app.libs.util.RecyclerTestAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.java.KoinJavaComponent

@RunWith(AndroidJUnit4::class)
@SmallTest
class SearchMoviePagingTest {
    private lateinit var recyclerView: RecyclerView
    private val query = "A"
    private val totalPage = 4

//    private val service: ApiService by lazy {
//        Retrofit.Builder()
//                .baseUrl("http://localhost:8080")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build().create(ApiService::class.java)
//    }

    private val service: MoviesNetwork by KoinJavaComponent.inject(MoviesNetwork::class.java)

    private val mappingCountCallHandler: HashMap<Int, Int> = HashMap<Int, Int>().apply {
        for (i in 0..totalPage) {
            this[i] = 0
        }
    }

    private val adapter: RecyclerTestAdapter<MovieItemResponse> by lazy {
        RecyclerTestAdapter()
    }

    private lateinit var pager: Flow<PagingData<MovieItemResponse>>

    private lateinit var mockWebServer: MockWebServer

    private val context: Context
        get() {
            return InstrumentationRegistry.getInstrumentation().targetContext
        }

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)

        recyclerView = RecyclerView(context)
        recyclerView.adapter = adapter

        mockWebServer.dispatcher = SearchMoviePagingDispatcher(context, ::receiveCallback)
        pager = Pager(
                config = PagingConfig(
                        pageSize = 20,
                        prefetchDistance = 3, // distance backward to get pages
                        enablePlaceholders = false,
                        initialLoadSize = 20
                ),
                pagingSourceFactory = { SearchMoviePagingSource(service, query) }
        ).flow
    }

    @After
    fun tearDown() {
        mockWebServer.dispatcher.shutdown()
        mockWebServer.shutdown()
    }

    @Test
    fun should_success_get_data_and_not_retrieve_anymore_page_if_not_reached_treshold() {
        runBlocking {
            val job = executeLaunch(this)
            delay(1000)
            adapter.forcePrefetch(10)
            delay(1000)

            Assert.assertEquals(1, mappingCountCallHandler[1])
            Assert.assertEquals(0, mappingCountCallHandler[2])
            Assert.assertEquals(20, adapter.itemCount)
            job.cancel()
        }
    }

    @Test
    fun should_success_retrieve_data_when_scrolldown_and_reached_treshold() {
        runBlocking {
            val job = executeLaunch(this)
            delay(1000)
            adapter.forcePrefetch(17)
            delay(1000)

            Assert.assertEquals(1, mappingCountCallHandler[2])
            Assert.assertEquals(40, adapter.itemCount)
            job.cancel()
        }
    }

    @Test
    fun should_not_get_data_again_when_pagination_end() {
        runBlocking {
            val job = executeLaunch(this)

            delay(1000)
            adapter.forcePrefetch(17)
            delay(1000)
            adapter.forcePrefetch(37)
            delay(1000)
            adapter.forcePrefetch(57)
            delay(1000)

            Assert.assertEquals(0, mappingCountCallHandler[totalPage])
            job.cancel()
        }
    }

    @Test
    fun should_success_get_data_when_scrollup_from_end() {
        runBlocking {

            val job = executeLaunch(this)

            // init from end, emulate user interaction
            delay(500)
            adapter.forcePrefetch(17)
            delay(500)
            adapter.forcePrefetch(37)
            delay(500)
            adapter.forcePrefetch(57)
            delay(500)

            adapter.forcePrefetch(37)
            delay(500)
            adapter.forcePrefetch(17)
            delay(500)

            Assert.assertEquals(1, mappingCountCallHandler[1])
            Assert.assertEquals(1, mappingCountCallHandler[2])
            Assert.assertEquals(1, mappingCountCallHandler[3])
            job.cancel()
        }
    }

    @Test
    fun should_refreshed_if_calling_refresh() {
        runBlocking {
            var job = executeLaunch(this)

            delay(1000)
            job.cancel()
            job = executeLaunch(this)
            delay(1000)

            Assert.assertEquals(2, mappingCountCallHandler[1])
            job.cancel()
        }
    }

    private fun executeLaunch(coroutineScope: CoroutineScope) = coroutineScope.launch {
        val res = pager.cachedIn(this)
        res.collectLatest {
            Log.d("SUBMIT", "submit data to adapter")
            adapter.submitData(it)
        }
    }

    private fun receiveCallback(reqPage: Int) {
        val prev = mappingCountCallHandler[reqPage]!!
        mappingCountCallHandler[reqPage] = prev + 1
    }
}
