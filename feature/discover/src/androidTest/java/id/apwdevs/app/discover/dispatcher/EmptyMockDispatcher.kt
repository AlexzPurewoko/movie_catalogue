package id.apwdevs.app.discover.dispatcher

import android.content.Context
import id.apwdevs.app.data.utils.Config
import id.apwdevs.app.libs.util.AssetDataJson
import id.apwdevs.app.libs.util.ScopeDispatcher

class EmptyMockDispatcher(context: Context): ScopeDispatcher(context) {
    private val apiKey = Config.TOKEN
    override val mappingRequest: Map<String, String>
        get() = mapOf(
            "/movie/popular?api_key=$apiKey&language=en-US&page=1" to AssetDataJson.DATA_EMPTY,
            "/movie/popular?api_key=$apiKey&language=en-US&page=2" to AssetDataJson.DATA_EMPTY,
            "/tv/popular?api_key=$apiKey&language=en-US&page=1" to AssetDataJson.DATA_EMPTY,
            "/tv/popular?api_key=$apiKey&language=en-US&page=2" to AssetDataJson.DATA_EMPTY,
            "/genre/movie/list?api_key=$apiKey&language=en-US" to AssetDataJson.DATA_EMPTY,
            "/genre/tv/list?api_key=$apiKey&language=en-US" to AssetDataJson.DATA_EMPTY
        )

}