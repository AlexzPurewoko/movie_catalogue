package id.apwdevs.app.data.paging.case

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import id.apwdevs.app.data.paging.test.RecyclerTestAdapter
import id.apwdevs.app.data.source.remote.paging.SearchMoviePagingSource
import id.apwdevs.app.data.source.remote.response.MovieItemResponse
import id.apwdevs.app.data.source.remote.service.ApiService
import kotlinx.coroutines.flow.Flow
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class SearchPagingTestCase <T: Any> {

    protected lateinit var recyclerView: RecyclerView
    protected val query = "A"
    protected val totalPage = 4
    protected val service: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://localhost:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java)
    }

    protected val mappingCountCallHandler: HashMap<Int, Int> = HashMap<Int, Int>().apply{
        for (i in 0..totalPage){
            this[i] = 0
        }
    }

    abstract val adapter: RecyclerTestAdapter<T>
    abstract val searchPagingSource: PagingSource<Int, T>

    protected lateinit var pager: Flow<PagingData<T>>

    protected lateinit var mockWebServer: MockWebServer

    protected val context : Context
        get() {
            return InstrumentationRegistry.getInstrumentation().targetContext
        }

    @Before
    open fun setup(){
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)

        recyclerView = RecyclerView(context)
        recyclerView.adapter = adapter

//        pager = Pager(
//            config = PagingConfig(
//                pageSize = 20,
//                prefetchDistance = 3, // distance backward to get pages
//                enablePlaceholders = false,
//                initialLoadSize = 20
//            ),
//            pagingSourceFactory = { searchPagingSource }
//        ).flow

    }

    @After
    open fun tearDown() {
        mockWebServer.dispatcher.shutdown()
        mockWebServer.shutdown()
    }

    @Test
    abstract fun should_success_get_data_and_not_retrieve_anymore_page_if_not_reached_treshold() 

    @Test
    abstract fun should_success_retrieve_data_when_scrolldown_and_reached_treshold() 

    @Test
    abstract fun should_not_get_data_again_when_pagination_end() 

    @Test
    abstract fun should_success_get_data_when_scrollup_from_end() 

    @Test
    abstract fun should_refreshed_if_calling_refresh()


    protected fun receiveCallback(reqPage: Int) {
        val prev = mappingCountCallHandler[reqPage]!!
        mappingCountCallHandler[reqPage] = prev + 1
    }
}