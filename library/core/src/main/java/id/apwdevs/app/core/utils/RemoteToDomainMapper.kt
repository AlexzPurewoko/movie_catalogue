package id.apwdevs.app.core.utils

import id.apwdevs.app.core.domain.model.*
import id.apwdevs.app.core.domain.model.detail.Creator
import id.apwdevs.app.core.domain.model.detail.EpisodeToAir
import id.apwdevs.app.core.domain.model.detail.ProductionCompany
import id.apwdevs.app.core.domain.model.detail.TvShowSeason
import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.remote.response.MovieItemResponse
import id.apwdevs.app.data.source.remote.response.TvShowItemResponse
import id.apwdevs.app.data.source.remote.response.moviedetail.MovieDetailResponse
import id.apwdevs.app.data.source.remote.response.tvdetail.TvDetailResponse
import id.apwdevs.app.data.source.remote.response.tvdetail.parts.CreatedByItem
import id.apwdevs.app.data.source.remote.response.tvdetail.parts.SeasonsItem

private fun genres(from: List<id.apwdevs.app.data.source.remote.response.parts.Genre>): List<Genre> =
    from.map { genre ->
        Genre(genre.id, genre.name)
    }

private fun productionCompanies(from: List<id.apwdevs.app.data.source.remote.response.parts.ProductionCompany>): List<ProductionCompany> =
    from.map { item ->
        ProductionCompany(item.id, item.logoPath, item.name, item.originCountry)
    }

private fun creators(from: List<CreatedByItem>): List<Creator> =
    from.map {
        Creator(it.id, it.gender, it.creditId, it.name, it.profilePath)
    }

private fun episodeToAir(from: id.apwdevs.app.data.source.remote.response.tvdetail.parts.EpisodesToAir?): EpisodeToAir? =
    from?.let {
        EpisodeToAir(
            id = it.id,
            productionCode = it.productionCode,
            airDate = it.airDate,
            overview = it.overview,
            episodeNumber = it.episodeNumber,
            voteAverage = it.voteAverage,
            name = it.name,
            seasonNumber = it.seasonNumber,
            stillPath = it.stillPath,
            voteCount = it.voteCount
        )
    }

private fun tvSeasons(from: List<SeasonsItem>): List<TvShowSeason> =
    from.map {
        TvShowSeason(
            it.id,
            it.airDate,
            it.overview,
            it.episodeCount,
            it.name,
            it.seasonNumber,
            it.posterPath
        )
    }


@JvmName("mapTvShowResponseToDomain")
fun TvShowItemResponse.mapToDomain(genres: Collection<Genres>): TvShow {
    val allGenres = genreIds.map {
        val item = genres.find { i -> i.id == it }
        if (item == null) Genre(0, "")
        else Genre(item.id, item.genreName)
    }
    return TvShow(
        tvId = id, name = name, firstAirDate = firstAirDate, overview = overview,
        language = originalLanguage, genres = allGenres, posterPath = posterPath,
        backdropPath = backdropPath, voteAverage = voteAverage, voteCount = voteCount
    )
}

@JvmName("mapMovieResponseToDomain")
fun MovieItemResponse.mapToDomain(genres: Collection<Genres>): Movies {
    val allGenres = genreIds.map {
        val item = genres.find { i -> i.id == it }
        if (item == null) Genre(0, "")
        else Genre(item.id, item.genreName)
    }
    return Movies(
        movieId = id, title = title, overview = overview,
        language = originalLanguage, genres = allGenres, posterPath = posterPath,
        backdropPath = backdropPath, releaseDate = releaseDate, voteAverage = voteAverage,
        voteCount = voteCount, adult = adult
    )
}

@JvmName("mapDetailTvResponseToDomain")
fun TvDetailResponse.mapToDomain(): DetailTvShow {
    val genres = genres(genres)
    val productionCompanies = productionCompanies(productionCompanies)
    val creators = creators(createdBy)
    val lastEpisode = episodeToAir(lastEpisodeToAir)
    val nextEpisode = episodeToAir(nextEpisodeToAir)
    val tvShowSeason = tvSeasons(seasons)

    return DetailTvShow(
        id = id,
        originalLanguage = originalLanguage,
        numberOfEpisodes = numberOfEpisodes,
        type = type,
        backdropPath = backdropPath,
        popularity = popularity,
        numberOfSeasons = numberOfSeasons,
        voteCount = voteCount,
        firstAirDate = firstAirDate,
        overview = overview,
        posterPath = posterPath,
        originalName = originalName,
        voteAverage = voteAverage,
        name = name,
        tagline = tagline,
        inProduction = inProduction,
        lastAirDate = lastAirDate,
        homepage = homepage,
        status = status,
        genres = genres,
        productionCompanies = productionCompanies,
        creators = creators,
        lastEpisodeToAir = lastEpisode!!,
        nextEpisodeToAir = nextEpisode,
        seasons = tvShowSeason
    )
}

@JvmName("mapDetailMovieResponseToDomain")
fun MovieDetailResponse.mapToDomain(): DetailMovie {
    val genres = genres(genres)
    val productionCompanies = productionCompanies(productionCompanies)
    return DetailMovie(
        movieId = id,
        originalLanguage = originalLanguage,
        title = title,
        backdropPath = backdropPath,
        revenue = revenue,
        popularity = popularity,
        voteCount = voteCount,
        budget = budget,
        overview = overview,
        originalTitle = originalTitle,
        runtime = runtime,
        posterPath = posterPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        tagline = tagline,
        adult = adult,
        homepage = homepage,
        status = status,
        genres = genres,
        productionCompanies = productionCompanies
    )
}