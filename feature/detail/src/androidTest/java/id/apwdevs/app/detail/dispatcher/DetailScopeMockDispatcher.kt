package id.apwdevs.app.detail.dispatcher

import android.content.Context
import android.util.Log
import id.apwdevs.app.data.utils.Config
import id.apwdevs.app.detail.data.AssetData
import id.apwdevs.app.libs.util.provideResponse
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

/**
 * Dispatch all the network request needed for scope DetailItemFragment
 * Must meet all the test case.
 */

class DetailScopeMockDispatcher(
    private val context: Context
) : Dispatcher() {

    private val mappingRequest = mapOf(
        "/movie/791373?api_key=${Config.TOKEN}&language=en-US" to AssetData.DETAIL_MOVIE_OK,
        "/tv/88396?api_key=${Config.TOKEN}&language=en-US" to AssetData.DETAIL_TVSHOW_OK,
        "/genre/movie/list?api_key=${Config.TOKEN}&language=en-US" to AssetData.GENRE_MOVIE_OK,
        "/genre/tv/list?api_key=${Config.TOKEN}&language=en-US" to AssetData.GENRE_TVSHOW_OK
    )

    override fun dispatch(request: RecordedRequest): MockResponse {
        Log.d("MockDispatcher", "Received Request Path: ${request.path}")
        val req = mappingRequest[request.path]
        return req?.let {
            context.provideResponse(it, 200)
        } ?: context.provideResponse(AssetData.DETAIL_404, 404)
    }

}