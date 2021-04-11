package id.apwdevs.app.detail.data

import id.apwdevs.app.core.domain.model.Genre
import id.apwdevs.app.core.domain.model.detail.TvShowSeason

data class TvShowDetail(
    val originalLanguage: String,
    val type: String,
    val backdropPath: String?,
    val firstAirDate: String,
    val overview: String,
    val posterPath: String?,
    val rating: Float,
    val title: String,
    val tagline: String,
    val status: String,
    val genres: List<Genre>,
    val lastEpisodeToAir: EpisodeItemData,
    val nextEpisodeToAir: EpisodeItemData?,
    val seasons: List<TvShowSeason>
)