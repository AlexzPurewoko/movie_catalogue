package id.apwdevs.app.data.source.remote.response.tvdetail.parts

import com.google.gson.annotations.SerializedName

data class CreatedByItem(

    @field:SerializedName("gender")
    val gender: Int,

    @field:SerializedName("credit_id")
    val creditId: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("profile_path")
    val profilePath: String?,

    @field:SerializedName("id")
    val id: Int
)