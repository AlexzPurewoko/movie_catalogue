package id.apwdevs.app.data.mediator.dispatcher

import android.content.Context
import id.apwdevs.app.data.utils.Config
import id.apwdevs.app.libs.util.AssetDataJson
import id.apwdevs.app.libs.util.ScopeDispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class PopularTvShowPagingDispatcher(
    context: Context,
    private val callback: (requestPage: Int) -> Unit
): ScopeDispatcher(context) {

    override val mappingRequest = mapOf(
        "/tv/popular?api_key=${Config.TOKEN}&language=en-US&page=1" to AssetDataJson.TVSHOW_PAGE_1,
        "/tv/popular?api_key=${Config.TOKEN}&language=en-US&page=2" to AssetDataJson.TVSHOW_PAGE_2,
        "/tv/popular?api_key=${Config.TOKEN}&language=en-US&page=3" to AssetDataJson.TVSHOW_PAGE_3,
        "/genre/tv/list?api_key=${Config.TOKEN}&language=en-US" to AssetDataJson.GENRE_TV
    )

    override fun dispatch(request: RecordedRequest): MockResponse {
        request.requestUrl?.queryParameter("page")?.let { callback(it.toInt()) }
        return super.dispatch(request)
    }
}