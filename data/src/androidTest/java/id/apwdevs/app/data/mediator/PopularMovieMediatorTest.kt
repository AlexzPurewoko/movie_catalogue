package id.apwdevs.app.data.mediator

import android.content.Context
import android.util.Log
import androidx.paging.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import id.apwdevs.app.data.paging.dispatcher.SearchMoviePagingDispatcher
import id.apwdevs.app.data.paging.test.RecyclerTestAdapter
import id.apwdevs.app.data.source.local.entity.items.MovieEntity
import id.apwdevs.app.data.source.local.room.AppDatabase
import id.apwdevs.app.data.source.remote.response.MovieItemResponse
import id.apwdevs.app.data.source.remote.service.ApiService
import id.apwdevs.app.data.util.AssertData
import id.apwdevs.app.data.util.provideResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import id.apwdevs.app.data.utils.Config

@RunWith(AndroidJUnit4::class)
@SmallTest
class PopularMovieMediatorTest {

    private val totalPage = 4

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val retrofitService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://localhost:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java)
    }

    private val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, "appdb.db")
            .build()
    }

    private lateinit var mockWebServer: MockWebServer

    private val mappingCountCallHandler: HashMap<Int, Int> = HashMap<Int, Int>().apply{
        for (i in 0..totalPage){
            this[i] = 0
        }
    }

    private val pagingSourceFactory = { appDatabase.movieDao().getAllMovies() }

    private lateinit var recyclerView: RecyclerView
    private lateinit var pager: Flow<PagingData<MovieEntity>>
    private val adapter: RecyclerTestAdapter<MovieEntity> by lazy {
        RecyclerTestAdapter()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)

        recyclerView = RecyclerView(context)
        recyclerView.adapter = adapter

        mockWebServer.dispatcher = PopularMoviePagingDispatcher(context, ::receiveCallback)
        pager = Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 3,
                initialLoadSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = PopularMovieRemoteMediator(
                retrofitService, appDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    @After
    fun tearDown() {
        mockWebServer.dispatcher.shutdown()
        mockWebServer.shutdown()
    }

    @Test
    fun firstLoad_should_load_first_data(){
        runBlocking {
            val job = executeLaunch(this)
            delay(1000)

            Assert.assertEquals(1, mappingCountCallHandler[1])
            Assert.assertEquals(20, adapter.itemCount)

            job.cancel()
        }
    }


    @Test
    fun refresh_should_get_data_again_from_api() {
        runBlocking {
            var job = executeLaunch(this)
            delay(1000)

            job.cancel()
            job = executeLaunch(this)
            delay(1000)

            Assert.assertEquals(2, mappingCountCallHandler[1])
            Assert.assertEquals(20, adapter.itemCount)

            job.cancel()
        }
    }

    @Test
    fun append_should_be_able_to_load_next_page_when_scrollUp() {
        runBlocking {
            var job = executeLaunch(this)
            delay(1000)

            adapter.forcePrefetch(18)

            Assert.assertEquals(1, mappingCountCallHandler[3])
            Assert.assertEquals(20, adapter.itemCount)

            job.cancel()
        }
    }

    @Test
    fun should_able_to_load_last_page_and_not_load_anymore() {
        runBlocking {
            var job = executeLaunch(this)
            delay(500)

            adapter.forcePrefetch(19)
            delay(500)
            adapter.forcePrefetch(38)
            delay(500)

            Assert.assertEquals(1, mappingCountCallHandler[1])
            Assert.assertEquals(1, mappingCountCallHandler[2])
            Assert.assertEquals(1, mappingCountCallHandler[3])
            Assert.assertEquals(0, mappingCountCallHandler[4])

            job.cancel()
        }
    }

    private fun receiveCallback(page: Int) {
        mappingCountCallHandler[page]?.let {
            val prev = it
            mappingCountCallHandler[page] = prev + 1
        }
    }

    private fun executeLaunch(coroutineScope: CoroutineScope) = coroutineScope.launch {
        val res = pager.cachedIn(this)
        res.collectLatest {
            Log.d("SUBMIT", "submit data to adapter")
            adapter.submitData(it)
        }
    }
}

class PopularMoviePagingDispatcher(
    private val context: Context,
    private val callback: (requestPage: Int) -> Unit
): Dispatcher() {

    private val mappingRequest = mapOf(
        "/movie/popular?api_key=${Config.TOKEN}&language=en-US&page=1" to AssertData.MOVIE_PAGE_1,
        "/movie/popular?api_key=${Config.TOKEN}&language=en-US&page=2" to AssertData.MOVIE_PAGE_2,
        "/movie/popular?api_key=${Config.TOKEN}&language=en-US&page=3" to AssertData.MOVIE_PAGE_3,
        "/genre/movie/list?api_key=${Config.TOKEN}&language=en-US" to AssertData.GENRE_MOVIES
    )
    override fun dispatch(request: RecordedRequest): MockResponse {
        Log.d("MockDispatcher", "Received Request Path: ${request.path}")
        val req = mappingRequest[request.path]
        request.requestUrl?.queryParameter("page")?.let { callback(it.toInt()) }
        return req?.let {
            context.provideResponse(it, 200)
        } ?: MockResponse().setResponseCode(422).setBody("{\n" +
                "    \"errors\": [\n" +
                "        \"page must be less than or equal to 3\"\n" +
                "    ]\n" +
                "}")
    }

}