package id.apwdevs.app.search.model

import id.apwdevs.app.core.domain.model.Genre

data class SearchItem(
    val id: Int,
    val title: String,
    val backdropImage: String?,
    val voteAverage: Double,
    val releaseDate: String
)