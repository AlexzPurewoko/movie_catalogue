package id.apwdevs.app.data.source.remote.network

import id.apwdevs.app.data.source.remote.response.GenreResponse
import id.apwdevs.app.data.source.remote.response.PageResponse
import id.apwdevs.app.data.source.remote.response.TvShowItemResponse
import id.apwdevs.app.data.source.remote.response.tvdetail.TvDetailResponse
import id.apwdevs.app.data.source.remote.service.ApiService
import id.apwdevs.app.data.utils.Config

class TvShowsNetworkImpl(
    private val apiService: ApiService
) : TvShowsNetwork {
    override suspend fun getPopularTvShows(
        page: Int,
        language: String
    ): PageResponse<TvShowItemResponse> {
        return apiService.getPopularTvShows(Config.TOKEN, language, page)
    }

    override suspend fun getDetailTvShows(movieId: String, language: String): TvDetailResponse {
        return apiService.getDetailTvShows(movieId, Config.TOKEN, language)
    }

    override suspend fun searchTvShows(
        query: String,
        includeAdult: Boolean,
        page: Int
    ): PageResponse<TvShowItemResponse> {
        return apiService.searchTvShow(Config.TOKEN, page, query, includeAdult)
    }

    override suspend fun getTvShowGenre(language: String): GenreResponse {
        return apiService.getTvShowGenre(Config.TOKEN, language)
    }

}