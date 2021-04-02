package id.apwdevs.app.data.paging

import android.graphics.Movie
import android.util.Log
import androidx.paging.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import id.apwdevs.app.data.paging.case.SearchPagingTestCase
import id.apwdevs.app.data.paging.dispatcher.SearchMoviePagingDispatcher
import id.apwdevs.app.data.paging.test.RecyclerTestAdapter
import id.apwdevs.app.data.source.remote.paging.SearchMoviePagingSource
import id.apwdevs.app.data.source.remote.response.MovieItemResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class SearchMoviePagingTest: SearchPagingTestCase<MovieItemResponse>() {

    override val adapter: RecyclerTestAdapter<MovieItemResponse>
        get() = RecyclerTestAdapter()

    override val searchPagingSource: PagingSource<Int, MovieItemResponse>
        get() = SearchMoviePagingSource(service, query)

    override fun setup() {
        super.setup()
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

    // treshold on 2 before end item pages
    override fun should_success_get_data_and_not_retrieve_anymore_page_if_not_reached_treshold() {
        runBlocking {
            val job = executeLaunch(this)
            job.start()
            delay(1000)
            adapter.forcePrefetch(10)
            delay(1000)

            Assert.assertEquals(1, mappingCountCallHandler[1])
            Assert.assertEquals(0, mappingCountCallHandler[2])
            Assert.assertEquals(20, adapter.itemCount)
            job.cancel()
        }
    }

    override fun should_success_retrieve_data_when_scrolldown_and_reached_treshold() {
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

    override fun should_not_get_data_again_when_pagination_end() {
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

    override fun should_success_get_data_when_scrollup_from_end() {
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

    override fun should_refreshed_if_calling_refresh() {
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

}


//@RunWith(AndroidJUnit4::class)
//@SmallTest
//class SearchTvShowPagingTest: SearchPagingTestCase() {
//
//    private lateinit var pager: Flow<PagingData<MovieItemResponse>>
//
//    override fun setup(){
//        super.setup()
//        mockWebServer.dispatcher = SearchMoviePagingDispatcher(context, ::receiveCallback)
//        pager = Pager(
//            config = PagingConfig(
//                pageSize = 20,
//                prefetchDistance = 3, // distance backward to get pages
//                enablePlaceholders = false,
//                initialLoadSize = 20
//            ),
//            pagingSourceFactory = {SearchMoviePagingSource(service, query)}
//        ).flow
//    }
//
//    override fun tearDown() {
//        mockWebServer.dispatcher.shutdown()
//        mockWebServer.shutdown()
//    }
//
//    // treshold on 2 before end item pages
//    override fun should_success_get_data_and_not_retrieve_anymore_page_if_not_reached_treshold() {
//        runBlocking {
//            val job = executeLaunch(this)
//            delay(1000)
//
//            Assert.assertEquals(1, mappingCountCallHandler[1])
//            Assert.assertEquals(0, mappingCountCallHandler[2])
//            Assert.assertEquals(20, adapter.itemCount)
//            job.cancel()
//        }
//    }
//
//    override fun should_success_retrieve_data_when_scrolldown_and_reached_treshold() {
//        runBlocking {
//            val job = executeLaunch(this)
//            delay(1000)
//            adapter.forcePrefetch(17)
//            delay(1000)
//
//            Assert.assertEquals(1, mappingCountCallHandler[2])
//            Assert.assertEquals(40, adapter.itemCount)
//            job.cancel()
//        }
//    }
//
//    override fun should_not_get_data_again_when_pagination_end() {
//        runBlocking {
//            val job = executeLaunch(this)
//
//            delay(1000)
//            adapter.forcePrefetch(17)
//            delay(1000)
//            adapter.forcePrefetch(37)
//            delay(1000)
//            adapter.forcePrefetch(57)
//            delay(1000)
//
//            Assert.assertEquals(0, mappingCountCallHandler[totalPage])
//            job.cancel()
//        }
//    }
//
//    override fun should_success_get_data_when_scrollup_from_end() {
//        runBlocking {
//
//            val job = executeLaunch(this)
//
//            // init from end, emulate user interaction
//            delay(500)
//            adapter.forcePrefetch(17)
//            delay(500)
//            adapter.forcePrefetch(37)
//            delay(500)
//            adapter.forcePrefetch(57)
//            delay(500)
//
//            adapter.forcePrefetch(37)
//            delay(500)
//            adapter.forcePrefetch(17)
//            delay(500)
//
//            Assert.assertEquals(1, mappingCountCallHandler[1])
//            Assert.assertEquals(1, mappingCountCallHandler[2])
//            Assert.assertEquals(1, mappingCountCallHandler[3])
//            job.cancel()
//        }
//    }
//
//    override fun should_refreshed_if_calling_refresh() {
//        runBlocking {
//            var job = executeLaunch(this)
//
//            delay(1000)
//            job.cancel()
//            job = executeLaunch(this)
//            delay(1000)
//
//            Assert.assertEquals(2, mappingCountCallHandler[1])
//            job.cancel()
//        }
//    }
//
//    private fun executeLaunch(coroutineScope: CoroutineScope) = coroutineScope.launch {
//        val res = pager.cachedIn(this)
//        res.collectLatest {
//            adapter.submitData(it)
//        }
//    }
//}