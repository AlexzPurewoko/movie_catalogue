package id.apwdevs.app.data.source.remote.response.tvdetail

import com.google.gson.annotations.SerializedName
import id.apwdevs.app.data.source.remote.response.parts.Genre
import id.apwdevs.app.data.source.remote.response.parts.ProductionCompany
import id.apwdevs.app.data.source.remote.response.parts.ProductionCountry
import id.apwdevs.app.data.source.remote.response.parts.SpokenLanguage
import id.apwdevs.app.data.source.remote.response.tvdetail.parts.CreatedByItem
import id.apwdevs.app.data.source.remote.response.tvdetail.parts.EpisodesToAir
import id.apwdevs.app.data.source.remote.response.tvdetail.parts.NetworksItem
import id.apwdevs.app.data.source.remote.response.tvdetail.parts.SeasonsItem

data class TvDetailResponse(

    @field:SerializedName("original_language")
	val originalLanguage: String,

    @field:SerializedName("number_of_episodes")
	val numberOfEpisodes: Int,

    @field:SerializedName("networks")
	val networks: List<NetworksItem>,

    @field:SerializedName("type")
	val type: String,

    @field:SerializedName("backdrop_path")
	val backdropPath: String?,

    @field:SerializedName("genres")
	val genres: List<Genre>,

    @field:SerializedName("popularity")
	val popularity: Double,

    @field:SerializedName("production_countries")
	val productionCountries: List<ProductionCountry>,

    @field:SerializedName("id")
	val id: Int,

    @field:SerializedName("number_of_seasons")
	val numberOfSeasons: Int,

    @field:SerializedName("vote_count")
	val voteCount: Int,

    @field:SerializedName("first_air_date")
	val firstAirDate: String,

    @field:SerializedName("overview")
	val overview: String,

    @field:SerializedName("seasons")
	val seasons: List<SeasonsItem>,

    @field:SerializedName("languages")
	val languages: List<String>,

    @field:SerializedName("created_by")
	val createdBy: List<CreatedByItem>,

    @field:SerializedName("last_episode_to_air")
	val lastEpisodeToAir: EpisodesToAir,

    @field:SerializedName("poster_path")
	val posterPath: String?,

    @field:SerializedName("origin_country")
	val originCountry: List<String>,

    @field:SerializedName("spoken_languages")
	val spokenLanguages: List<SpokenLanguage>,

    @field:SerializedName("production_companies")
	val productionCompanies: List<ProductionCompany>,

    @field:SerializedName("original_name")
	val originalName: String,

    @field:SerializedName("vote_average")
	val voteAverage: Double,

    @field:SerializedName("name")
	val name: String,

    @field:SerializedName("tagline")
	val tagline: String,

    @field:SerializedName("episode_run_time")
	val episodeRunTime: List<Int>,

    @field:SerializedName("next_episode_to_air")
	val nextEpisodeToAir: EpisodesToAir?,

    @field:SerializedName("in_production")
	val inProduction: Boolean,

    @field:SerializedName("last_air_date")
	val lastAirDate: String,

    @field:SerializedName("homepage")
	val homepage: String,

    @field:SerializedName("status")
	val status: String
)