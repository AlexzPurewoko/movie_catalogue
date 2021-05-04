package id.apwdevs.app.data.utils

import id.apwdevs.app.data.source.local.entity.converters.GenreIdsTypeConverter
import id.apwdevs.app.data.source.local.entity.items.MovieEntity
import id.apwdevs.app.data.source.local.entity.items.TvEntity
import id.apwdevs.app.data.source.remote.response.MovieItemResponse
import id.apwdevs.app.data.source.remote.response.TvShowItemResponse

@JvmName("mapToEntityFromMovieItemResponse")
fun Collection<MovieItemResponse>.mapToEntity(page: Int): Collection<MovieEntity> {
    return this.map {
        val convertedGenres = GenreIdsTypeConverter.GenreIdData(
            data = it.genreIds
        )
        MovieEntity(
            id = it.id,
            title = it.title,
            overview = it.overview,
            language = it.originalLanguage,
            genreIds = convertedGenres,
            posterPath = it.posterPath,
            backdropPath = it.backdropPath,
            releaseDate = it.releaseDate,
            voteAverage = it.voteAverage,
            voteCount = it.voteCount,
            adult = it.adult,
            page = page
        )
    }
}

@JvmName("mapToEntityFromTvShowItemResponse")
fun Collection<TvShowItemResponse>.mapToEntity(page: Int): Collection<TvEntity> {
    return this.map {
        val convertedGenres = GenreIdsTypeConverter.GenreIdData(
            data = it.genreIds
        )

        TvEntity(
            id = it.id,
            name = it.name,
            overview = it.overview,
            language = it.originalLanguage,
            genreIds = convertedGenres,
            posterPath = it.posterPath,
            backdropPath = it.backdropPath,
            firstAirDate = it.firstAirDate,
            voteAverage = it.voteAverage,
            voteCount = it.voteCount,
            page = page
        )
    }
}