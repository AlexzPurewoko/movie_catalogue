package id.apwdevs.app.detail.data

import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.Genre
import id.apwdevs.app.core.domain.model.detail.ProductionCompany

data class MovieDetail (
    val originalLanguage: String,
    val title: String,
    val backdropPath: String?,
    val overview: String?,
    val runTime: String, // format 1h 40m
    val posterPath: String?,
    val releaseDate: String,
    val rating: Float,
    val tagline: String?,
    val status: String,
    val genres: List<Genre>,
)