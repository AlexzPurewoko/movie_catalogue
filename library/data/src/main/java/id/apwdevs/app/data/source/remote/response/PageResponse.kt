package id.apwdevs.app.data.source.remote.response

import com.google.gson.annotations.SerializedName
import id.apwdevs.app.data.source.remote.response.parts.ItemResponse

data class PageResponse<T : ItemResponse>(

    @field:SerializedName("page")
    val page: Int,

    @field:SerializedName("total_pages")
    val totalPages: Int,

    @field:SerializedName("results")
    val results: List<T>,

    @field:SerializedName("total_results")
    val totalResults: Int
)