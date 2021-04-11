package id.apwdevs.app.data.mediator.dispatcher

import android.content.Context
import android.util.Log
import id.apwdevs.app.data.util.AssertData
import id.apwdevs.app.data.util.provideResponse
import id.apwdevs.app.data.utils.Config
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

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