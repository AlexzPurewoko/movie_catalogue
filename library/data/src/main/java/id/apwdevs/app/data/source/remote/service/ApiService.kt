package id.apwdevs.app.data.source.remote.service

import android.opengl.GLES32
import id.apwdevs.app.data.source.remote.response.GenreResponse
import id.apwdevs.app.data.source.remote.response.MovieItemResponse
import id.apwdevs.app.data.source.remote.response.PageResponse
import id.apwdevs.app.data.source.remote.response.TvShowItemResponse
import id.apwdevs.app.data.source.remote.response.moviedetail.MovieDetailResponse
import id.apwdevs.app.data.source.remote.response.tvdetail.TvDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@Deprecated("use for internal only", level = DeprecationLevel.WARNING)
interface ApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): PageResponse<MovieItemResponse>

    @GET("tv/popular")
    suspend fun getPopularTvShows(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): PageResponse<TvShowItemResponse>

    @GET("movie/{id}")
    suspend fun getDetailMovies(
        @Path("id") id: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
    ): MovieDetailResponse

    @GET("tv/{id}")
    suspend fun getDetailTvShows(
        @Path("id") id: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
    ): TvDetailResponse

    @GET("search/tv")
    suspend fun searchTvShow(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
        @Query("query") querySearch: String,
        @Query("include_adult") includeAdult: Boolean,
    ): PageResponse<TvShowItemResponse>


    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
        @Query("query") querySearch: String,
        @Query("include_adult") includeAdult: Boolean,
    ): PageResponse<MovieItemResponse>

    @GET("genre/movie/list")
    suspend fun getMovieGenre(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): GenreResponse

    @GET("genre/tv/list")
    suspend fun getTvShowGenre(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): GenreResponse
}
