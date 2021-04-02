package id.apwdevs.app.core.domain.model

import id.apwdevs.app.core.domain.model.detail.Creator
import id.apwdevs.app.core.domain.model.detail.EpisodeToAir
import id.apwdevs.app.core.domain.model.detail.ProductionCompany
import id.apwdevs.app.core.domain.model.detail.TvShowSeason

data class DetailTvShow(
    val id: Int,
    val originalLanguage: String,
    val numberOfEpisodes: Int,
    val type: String,
    val backdropPath: String?,
    val popularity: Double,
    val numberOfSeasons: Int,
    val voteCount: Int,
    val firstAirDate: String,
    val overview: String,
    val posterPath: String?,
    val originalName: String,
    val voteAverage: Double,
    val name: String,
    val tagline: String,
    val inProduction: Boolean,
    val lastAirDate: String,
    val homepage: String?,
    val status: String,
    val genres: List<Genre>,
    val productionCompanies: List<ProductionCompany>,
    val creators: List<Creator>,
    val lastEpisodeToAir: EpisodeToAir,
    val nextEpisodeToAir: EpisodeToAir?,
    val seasons: List<TvShowSeason>
)