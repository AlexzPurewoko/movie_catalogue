package id.apwdevs.app.data.source.remote.response.moviedetail

import com.google.gson.annotations.SerializedName
import id.apwdevs.app.data.source.remote.response.moviedetail.parts.BelongsToCollection
import id.apwdevs.app.data.source.remote.response.parts.Genre
import id.apwdevs.app.data.source.remote.response.parts.ProductionCompany
import id.apwdevs.app.data.source.remote.response.parts.ProductionCountry
import id.apwdevs.app.data.source.remote.response.parts.SpokenLanguage

data class MovieDetailResponse(

    @field:SerializedName("original_language")
    val originalLanguage: String,

    @field:SerializedName("imdb_id")
    val imdbId: String?,

    @field:SerializedName("video")
    val video: Boolean,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("backdrop_path")
    val backdropPath: String?,

    @field:SerializedName("revenue")
    val revenue: Int,

    @field:SerializedName("genres")
    val genres: List<Genre>,

    @field:SerializedName("popularity")
    val popularity: Double,

    @field:SerializedName("production_countries")
    val productionCountries: List<ProductionCountry>,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("vote_count")
    val voteCount: Int,

    @field:SerializedName("budget")
    val budget: Int,

    @field:SerializedName("overview")
    val overview: String?,

    @field:SerializedName("original_title")
    val originalTitle: String,

    @field:SerializedName("runtime")
    val runtime: Int?,

    @field:SerializedName("poster_path")
    val posterPath: String?,

    @field:SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage>,

    @field:SerializedName("production_companies")
    val productionCompanies: List<ProductionCompany>,

    @field:SerializedName("release_date")
    val releaseDate: String,

    @field:SerializedName("vote_average")
    val voteAverage: Double,

    @field:SerializedName("belongs_to_collection")
    val belongsToCollection: BelongsToCollection?,

    @field:SerializedName("tagline")
    val tagline: String?,

    @field:SerializedName("adult")
    val adult: Boolean,

    @field:SerializedName("homepage")
    val homepage: String?,

    @field:SerializedName("status")
    val status: String
)