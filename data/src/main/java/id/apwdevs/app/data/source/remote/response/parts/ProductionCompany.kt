package id.apwdevs.app.data.source.remote.response.parts

import com.google.gson.annotations.SerializedName

data class ProductionCompany(

	@field:SerializedName("logo_path")
	val logoPath: String?,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("origin_country")
	val originCountry: String
)