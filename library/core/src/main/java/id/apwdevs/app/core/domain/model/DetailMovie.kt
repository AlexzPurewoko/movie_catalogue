package id.apwdevs.app.core.domain.model

import id.apwdevs.app.core.domain.model.detail.ProductionCompany

data class DetailMovie(
    val movieId: Int,
    val originalLanguage: String,
    val title: String,
    val backdropPath: String?,
    val revenue: Int,
    val popularity: Double,
    val voteCount: Int,
    val budget: Int,
    val overview: String?,
    val originalTitle: String,
    val runtime: Int?,
    val posterPath: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val tagline: String?,
    val adult: Boolean,
    val homepage: String?,
    val status: String,
    val genres: List<Genre>,
    val productionCompanies: List<ProductionCompany>
)

