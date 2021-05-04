package id.apwdevs.app.res.data

import id.apwdevs.app.core.domain.model.Genre

data class MovieShowItem(
    val id: Int,
    val title: String,
    val overview: String?,
    val backdopImage: String?,
    val genres: List<Genre>,
    val voteCount: Int,
    val voteAverage: Double
)