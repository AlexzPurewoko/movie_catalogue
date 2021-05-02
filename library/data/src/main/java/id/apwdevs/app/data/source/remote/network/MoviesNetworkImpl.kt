package id.apwdevs.app.data.source.remote.network

import id.apwdevs.app.data.source.remote.response.GenreResponse
import id.apwdevs.app.data.source.remote.response.MovieItemResponse
import id.apwdevs.app.data.source.remote.response.PageResponse
import id.apwdevs.app.data.source.remote.response.moviedetail.MovieDetailResponse
import id.apwdevs.app.data.source.remote.service.ApiService
import id.apwdevs.app.data.utils.Config

class MoviesNetworkImpl(
    private val apiService: ApiService
): MoviesNetwork {

    override suspend fun getPopularMovies(page: Int, language: String): PageResponse<MovieItemResponse> {
        return apiService.getPopularMovies(Config.TOKEN, language, page)
    }

    override suspend fun getDetailMovies(movieId: String, language: String): MovieDetailResponse {
        return apiService.getDetailMovies(movieId, Config.TOKEN, language)
    }

    override suspend fun searchMovies(query: String, includeAdult: Boolean, page: Int): PageResponse<MovieItemResponse> {
        return apiService.searchMovies(Config.TOKEN, page, query, includeAdult)
    }

    override suspend fun getMovieGenre(language: String): GenreResponse {
        return apiService.getMovieGenre(Config.TOKEN, language)
    }
}