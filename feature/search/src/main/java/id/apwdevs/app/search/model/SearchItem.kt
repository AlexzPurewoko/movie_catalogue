package id.apwdevs.app.search.model

data class SearchItem(
    val id: Int,
    val title: String,
    val backdropImage: String?,
    val voteAverage: Double,
    val releaseDate: String
)