package id.apwdevs.app.detail.dispatcher

import android.content.Context
import id.apwdevs.app.data.utils.Config
import id.apwdevs.app.detail.data.AssetData
import id.apwdevs.app.libs.util.ScopeDispatcher

/**
 * Dispatch all the network request needed for scope DetailItemFragment
 * Must meet all the test case.
 */

class DetailScopeMockDispatcher(
    context: Context
) : ScopeDispatcher(context) {

    override val mappingRequest = mapOf(
        "/movie/791373?api_key=${Config.TOKEN}&language=en-US" to AssetData.DETAIL_MOVIE_OK,
        "/tv/88396?api_key=${Config.TOKEN}&language=en-US" to AssetData.DETAIL_TVSHOW_OK,
        "/genre/movie/list?api_key=${Config.TOKEN}&language=en-US" to AssetData.GENRE_MOVIE_OK,
        "/genre/tv/list?api_key=${Config.TOKEN}&language=en-US" to AssetData.GENRE_TVSHOW_OK
    )
}