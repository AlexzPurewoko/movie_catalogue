package id.apwdevs.app.search

import android.content.Context
import id.apwdevs.app.data.utils.Config
import id.apwdevs.app.libs.util.AssetDataJson
import id.apwdevs.app.libs.util.ScopeDispatcher

/**
 * Dispatch all the network request needed for scope SearchPage
 * Must meet all the test case.
 */
class SearchMockDispatcher(
    context: Context
) : ScopeDispatcher(context) {
    override val mappingRequest: Map<String, String>
        get() = mapOf(
            "/search/movie?api_key=${Config.TOKEN}&page=1&query=a&include_adult=false" to AssetDataJson.MOVIE_PAGE_1,
            "/search/movie?api_key=${Config.TOKEN}&page=2&query=a&include_adult=false" to AssetDataJson.MOVIE_PAGE_2,
            "/search/tv?api_key=${Config.TOKEN}&page=1&query=a&include_adult=false" to AssetDataJson.TVSHOW_PAGE_1,
            "/search/tv?api_key=${Config.TOKEN}&page=2&query=a&include_adult=false" to AssetDataJson.TVSHOW_PAGE_1,
            "/genre/movie/list?api_key=${Config.TOKEN}&language=en-US" to AssetDataJson.GENRE_MOVIES,
            "/genre/tv/list?api_key=${Config.TOKEN}&language=en-US" to AssetDataJson.GENRE_TV
        )

}