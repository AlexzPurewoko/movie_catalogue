package id.apwdevs.app.core.utils

import id.apwdevs.app.core.domain.model.*
import id.apwdevs.app.core.domain.model.detail.Creator
import id.apwdevs.app.core.domain.model.detail.EpisodeToAir
import id.apwdevs.app.core.domain.model.detail.ProductionCompany
import id.apwdevs.app.core.domain.model.detail.TvShowSeason
import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.local.entity.detail.ProductionCompanies
import id.apwdevs.app.data.source.local.entity.detail.movie.FavDetailMovie
import id.apwdevs.app.data.source.local.entity.detail.movie.FavDetailMovieEntity
import id.apwdevs.app.data.source.local.entity.detail.tvshow.CreatedByEntity
import id.apwdevs.app.data.source.local.entity.detail.tvshow.EpisodesToAir
import id.apwdevs.app.data.source.local.entity.detail.tvshow.FavDetailTvShow
import id.apwdevs.app.data.source.local.entity.detail.tvshow.FavDetailTvShowEntity
import id.apwdevs.app.data.source.local.entity.items.MovieEntity
import id.apwdevs.app.data.source.local.entity.items.TvEntity
import id.apwdevs.app.data.source.local.entity.detail.tvshow.TvShowSeason as EntityTvShowSeason

private fun genres(from: List<Genres>): List<Genre> =
    from.map {
        Genre(it.id, it.genreName)
    }

private fun productionCompanies(from: List<ProductionCompanies>): List<ProductionCompany> =
    from.map {
        ProductionCompany(it.productionId, it.logoPath, it.name, it.originCountry)
    }

private fun creators(from: List<CreatedByEntity>): List<Creator> =
    from.map {
        Creator(
            id = it.id,
            gender = it.gender,
            creditId = it.creditId,
            name = it.name,
            profilePath = it.profilePath,
        )
    }

private fun seasons(from: List<EntityTvShowSeason>): List<TvShowSeason> =
    from.map {
        TvShowSeason(
            id = it.id,
            airDate = it.airDate,
            overview = it.overview,
            episodeCount = it.episodeCount,
            name = it.name,
            seasonNumber = it.seasonNumber,
            posterPath = it.posterPath
        )
    }

private fun episodeToAir(from: EpisodesToAir?) =
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

@JvmName("fromFavDetailMovieEntityIntoDomainMovies")
suspend fun Collection<FavDetailMovieEntity>.mapToDomain(
    genres: Collection<Genres>,
    genreMapper: suspend (movieId: Int) -> Collection<Int>
): Collection<Movies> {
    return this.map { entity ->
        val domainGenres = genreMapper(entity.id).map {
            val item = genres.find { i -> i.id == it }
            if (item == null) Genre(0, "")
            else Genre(item.id, item.genreName)
        }

        Movies(
            movieId = entity.id,
            title = entity.title,
            overview = entity.overview,
            language = entity.originalLanguage,
            genres = domainGenres,
            posterPath = entity.posterPath,
            backdropPath = entity.backdropPath,
            releaseDate = entity.releaseDate,
            voteAverage = entity.voteAverage,
            voteCount = entity.voteCount,
            adult = entity.adult
        )
    }
}

@JvmName("fromFavDetailTvShowEntityIntoDomainTvShowList")
suspend fun Collection<FavDetailTvShowEntity>.mapToDomain(
    genres: Collection<Genres>,
    genreMapper: suspend (movieId: Int) -> Collection<Int>
): Collection<TvShow> {
    return map { entity ->
        val domainGenres = genreMapper(entity.id).map {
            val item = genres.find { i -> i.id == it }
            if (item == null) Genre(0, "")
            else Genre(item.id, item.genreName)
        }

        TvShow(
            tvId = entity.id,
            name = entity.name,
            firstAirDate = entity.firstAirDate,
            overview = entity.overview,
            language = entity.originalLanguage,
            genres = domainGenres,
            posterPath = entity.posterPath,
            backdropPath = entity.backdropPath,
            voteAverage = entity.voteAverage,
            voteCount = entity.voteCount
        )
    }
}

fun TvEntity.mapToDomain(genres: Collection<Genres>): TvShow {
    val allGenres = genreIds.data.map {
        val item = genres.find { i -> i.id == it }
        if (item == null) Genre(0, "")
        else Genre(item.id, item.genreName)
    }
    return TvShow(
        tvId = id, name = name, firstAirDate = firstAirDate, overview = overview,
        language = language, genres = allGenres, posterPath = posterPath,
        backdropPath = backdropPath, voteAverage = voteAverage, voteCount = voteCount
    )
}

fun MovieEntity.mapToDomain(genres: Collection<Genres>): Movies {
    val allGenres = genreIds.data.map {
        val item = genres.find { i -> i.id == it }
        if (item == null) Genre(0, "")
        else Genre(item.id, item.genreName)
    }
    return Movies(
        movieId = id, title = title, overview = overview,
        language = language, genres = allGenres, posterPath = posterPath,
        backdropPath = backdropPath, releaseDate = releaseDate, voteAverage = voteAverage,
        voteCount = voteCount, adult = adult
    )
}

@JvmName("mapDetailMovieEntityIntoDomain")
fun FavDetailMovie.mapToDomain(): DetailMovie {
    val i = detailMovie
    return DetailMovie(
        movieId = i.id,
        originalLanguage = i.originalLanguage,
        title = i.title,
        backdropPath = i.backdropPath,
        revenue = i.revenue,
        popularity = i.popularity,
        voteCount = i.voteCount,
        budget = i.budget,
        overview = i.overview,
        originalTitle = i.originalTitle,
        runtime = i.runtime,
        posterPath = i.posterPath,
        releaseDate = i.releaseDate,
        voteAverage = i.voteAverage,
        tagline = i.tagline,
        adult = i.adult,
        homepage = i.homepage,
        status = i.status,
        genres = genres(
            genres
        ),
        productionCompanies = productionCompanies(productionCompanies)
    )
}

@JvmName("mapDetailTvShowEntityIntoDomain")
fun FavDetailTvShow.mapToDomain(): DetailTvShow {
    val entity = favDetailTvShow
    val genre = genres(genres)
    val creator = creators(creators)
    val lastEpisodeToAir =
        lastAndNextEpisodeToAir.find { i -> i.airingType == EpisodesToAir.AiringType.LAST_AIR }
    val nextEpisodeToAir =
        lastAndNextEpisodeToAir.find { i -> i.airingType == EpisodesToAir.AiringType.NEXT_AIR }
    val productionCompany = productionCompanies(productionCompanies)
    val season = seasons(seasons)
    return DetailTvShow(
        id = entity.id,
        originalLanguage = entity.originalLanguage,
        numberOfEpisodes = entity.numberOfEpisodes,
        type = entity.type,
        backdropPath = entity.backdropPath,
        popularity = entity.popularity,
        numberOfSeasons = entity.numberOfSeasons,
        voteCount = entity.voteCount,
        firstAirDate = entity.firstAirDate,
        overview = entity.overview,
        posterPath = entity.posterPath,
        originalName = entity.originalName,
        voteAverage = entity.voteAverage,
        name = entity.name,
        tagline = entity.tagline,
        inProduction = entity.inProduction,
        lastAirDate = entity.lastAirDate,
        homepage = entity.homepage,
        status = entity.status,
        genres = genre,
        productionCompanies = productionCompany,
        creators = creator,
        lastEpisodeToAir = episodeToAir(
            lastEpisodeToAir
        )!!,
        nextEpisodeToAir = episodeToAir(nextEpisodeToAir),
        seasons = season
    )
}