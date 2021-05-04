package id.apwdevs.app.discover.dispatcher

import android.content.Context
import id.apwdevs.app.data.utils.Config
import id.apwdevs.app.libs.util.AssetDataJson
import id.apwdevs.app.libs.util.ScopeDispatcher

class DiscoverMockDispatcher(context: Context) : ScopeDispatcher(context) {

    private val apiKey = Config.TOKEN

    override val mappingRequest: Map<String, String>
        get() = mapOf(
            "/movie/popular?api_key=$apiKey&language=en-US&page=1" to AssetDataJson.MOVIE_PAGE_1,
            "/movie/popular?api_key=$apiKey&language=en-US&page=2" to AssetDataJson.MOVIE_PAGE_2,
            "/tv/popular?api_key=$apiKey&language=en-US&page=1" to AssetDataJson.TVSHOW_PAGE_1,
            "/tv/popular?api_key=$apiKey&language=en-US&page=2" to AssetDataJson.TVSHOW_PAGE_2,
            "/genre/movie/list?api_key=$apiKey&language=en-US" to AssetDataJson.GENRE_MOVIES,
            "/genre/tv/list?api_key=$apiKey&language=en-US" to AssetDataJson.GENRE_TV
        )
}

