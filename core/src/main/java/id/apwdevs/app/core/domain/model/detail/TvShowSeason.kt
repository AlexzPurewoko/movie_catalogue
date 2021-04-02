package id.apwdevs.app.core.domain.model.detail


data class TvShowSeason(
    val id: Int,
    val airDate: String,
    val overview: String,
    val episodeCount: Int,
    val name: String,
    val seasonNumber: Int,
    val posterPath: String?
)
