package id.apwdevs.app.core.domain.model

data class Movies(
    val movieId: Int,
    val title: String,
    val overview: String?,
    val language: String,
    val genres: List<Genre>,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val adult: Boolean
)

