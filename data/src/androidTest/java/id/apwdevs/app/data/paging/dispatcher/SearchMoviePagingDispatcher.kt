package id.apwdevs.app.data.paging.dispatcher

import android.content.Context
import android.util.Log
import id.apwdevs.app.data.util.AssertData
import id.apwdevs.app.data.util.provideResponse
import id.apwdevs.app.data.utils.Config
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class SearchMoviePagingDispatcher(
    private val context: Context,
    private val callback: (requestPage: Int) -> Unit
): Dispatcher() {

    private val mappingRequest = mapOf(
        "/search/movie?api_key=${Config.TOKEN}&page=1&query=A&include_adult=false" to AssertData.MOVIE_PAGE_1,
        "/search/movie?api_key=${Config.TOKEN}&page=2&query=A&include_adult=false" to AssertData.MOVIE_PAGE_2,
        "/search/movie?api_key=${Config.TOKEN}&page=3&query=A&include_adult=false" to AssertData.MOVIE_PAGE_3,
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

