package id.apwdevs.app.data.source.remote.response.parts

import com.google.gson.annotations.SerializedName

data class SpokenLanguage(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("iso_639_1")
    val iso6391: String,

    @field:SerializedName("english_name")
    val englishName: String?
)