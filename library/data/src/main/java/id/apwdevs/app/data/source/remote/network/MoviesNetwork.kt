package id.apwdevs.app.data.source.remote.network

import id.apwdevs.app.data.source.remote.response.GenreResponse
import id.apwdevs.app.data.source.remote.response.MovieItemResponse
import id.apwdevs.app.data.source.remote.response.PageResponse
import id.apwdevs.app.data.source.remote.response.moviedetail.MovieDetailResponse


/**
 * These interfaces are collection methods to get movie data from API.
 */
interface MoviesNetwork {
    suspend fun getPopularMovies(
        page: Int,
        language: String = "en-US"
    ): PageResponse<MovieItemResponse>

    suspend fun getDetailMovies(movieId: String, language: String = "en-US"): MovieDetailResponse
    suspend fun searchMovies(
        query: String,
        includeAdult: Boolean,
        page: Int = 1
    ): PageResponse<MovieItemResponse>

    suspend fun getMovieGenre(language: String = "en-US"): GenreResponse
}