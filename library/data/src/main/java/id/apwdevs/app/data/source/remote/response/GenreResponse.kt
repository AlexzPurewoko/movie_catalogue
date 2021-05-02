package id.apwdevs.app.data.source.remote.response

import com.google.gson.annotations.SerializedName
import id.apwdevs.app.data.source.remote.response.parts.Genre

data class GenreResponse(

	@field:SerializedName("genres")
	val genres: List<Genre>?
)
