package id.apwdevs.app.core.domain.model.detail

data class EpisodeToAir(
    val id: Int,
    val productionCode: String,
    val airDate: String,
    val overview: String,
    val episodeNumber: Int,
    val voteAverage: Double,
    val name: String,
    val seasonNumber: Int,
    val stillPath: String?,
    val voteCount: Int
)
