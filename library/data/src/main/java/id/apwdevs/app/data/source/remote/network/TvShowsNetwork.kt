package id.apwdevs.app.data.source.remote.network

import id.apwdevs.app.data.source.remote.response.GenreResponse
import id.apwdevs.app.data.source.remote.response.PageResponse
import id.apwdevs.app.data.source.remote.response.TvShowItemResponse
import id.apwdevs.app.data.source.remote.response.tvdetail.TvDetailResponse

/**
 * These interfaces are collection methods to get TvShows data from API.
 */
interface TvShowsNetwork {
    suspend fun getPopularTvShows(page: Int, language: String = "en-US") : PageResponse<TvShowItemResponse>
    suspend fun getDetailTvShows(movieId: String, language: String = "en-US") : TvDetailResponse
    suspend fun searchTvShows(query: String, includeAdult: Boolean, page: Int = 1) : PageResponse<TvShowItemResponse>
    suspend fun getTvShowGenre(language: String = "en-US") : GenreResponse
}

