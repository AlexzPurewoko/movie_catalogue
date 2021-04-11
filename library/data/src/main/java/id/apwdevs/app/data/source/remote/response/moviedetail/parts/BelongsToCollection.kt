package id.apwdevs.app.data.source.remote.response.moviedetail.parts

import com.google.gson.annotations.SerializedName

data class BelongsToCollection(

	@field:SerializedName("backdrop_path")
	val backdropPath: String?,

	@field:SerializedName("name")
	val name: String?,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("poster_path")
	val posterPath: String?
)