package id.apwdevs.app.data.source.remote.response.tvdetail.parts

import com.google.gson.annotations.SerializedName

data class EpisodesToAir(

    @field:SerializedName("production_code")
    val productionCode: String,

    @field:SerializedName("air_date")
    val airDate: String,

    @field:SerializedName("overview")
    val overview: String,

    @field:SerializedName("episode_number")
    val episodeNumber: Int,

    @field:SerializedName("vote_average")
    val voteAverage: Double,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("season_number")
    val seasonNumber: Int,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("still_path")
    val stillPath: String?,

    @field:SerializedName("vote_count")
    val voteCount: Int
)