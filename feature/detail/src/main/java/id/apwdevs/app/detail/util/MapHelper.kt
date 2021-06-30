package id.apwdevs.app.detail.util

import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.detail.EpisodeToAir
import id.apwdevs.app.detail.data.EpisodeItemData
import id.apwdevs.app.detail.data.MovieDetail
import id.apwdevs.app.detail.data.TvShowDetail
import id.apwdevs.app.res.util.zeroIfNull
import java.util.*
import kotlin.math.floor

private fun composeEpisode(lastEpisodeToAir: EpisodeToAir): EpisodeItemData {
    return EpisodeItemData(
        lastEpisodeToAir.stillPath,
        lastEpisodeToAir.name,
        lastEpisodeToAir.voteAverage,
        lastEpisodeToAir.airDate,
        lastEpisodeToAir.seasonNumber
    )
}

private fun composeRuntime(runtime: Int?): String {
    val runTime = runtime.zeroIfNull
    return if (runTime == 0) "0h 0m"
    else {
        val hours = floor(runTime.toDouble() / 60).toInt()
        val minutes = runTime % 60

        "${hours}h ${minutes}m"
    }
}

private fun getLanguage(iso6391: String): String {
    return Locale(iso6391).displayName ?: " - "
}

@JvmName("mapFromDomainDetailMovieIntoItem")
fun DetailMovie.mapToItem(): MovieDetail {
    return MovieDetail(
        originalLanguage = getLanguage(originalLanguage),
        title = title,
        backdropPath = backdropPath,
        overview = overview,
        runTime = composeRuntime(runtime),
        posterPath = posterPath,
        releaseDate = releaseDate,
        rating = voteAverage.toFloat(),
        status = status,
        genres = genres
    )
}

@JvmName("mapFromDomainDetailTvShowIntoItem")
fun DetailTvShow.mapToItem(): TvShowDetail {
    return TvShowDetail(
        originalLanguage = getLanguage(originalLanguage),
        title = name,
        backdropPath = backdropPath,
        overview = overview,
        posterPath = posterPath,
        firstAirDate = firstAirDate,
        rating = voteAverage.toFloat(),
        status = status,
        genres = genres,
        lastEpisodeToAir = composeEpisode(lastEpisodeToAir),
        nextEpisodeToAir = nextEpisodeToAir?.let { n -> composeEpisode(n) },
        seasons = seasons,
        type = type
    )
}