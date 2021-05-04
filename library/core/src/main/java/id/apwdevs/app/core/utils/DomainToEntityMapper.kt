package id.apwdevs.app.core.utils

import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.Genre
import id.apwdevs.app.core.domain.model.detail.Creator
import id.apwdevs.app.core.domain.model.detail.EpisodeToAir
import id.apwdevs.app.core.domain.model.detail.ProductionCompany
import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.local.entity.detail.ProductionCompanies
import id.apwdevs.app.data.source.local.entity.detail.movie.FavDetailMovie
import id.apwdevs.app.data.source.local.entity.detail.movie.FavDetailMovieEntity
import id.apwdevs.app.data.source.local.entity.detail.tvshow.*


/**
 * Domain to Entity mapper
 */

private fun extractTvShowEntityFromDomain(from: DetailTvShow): FavDetailTvShowEntity =
    from.let {
        FavDetailTvShowEntity(
            id = it.id,
            originalLanguage = it.originalLanguage,
            numberOfEpisodes = it.numberOfEpisodes,
            type = it.type,
            backdropPath = it.backdropPath,
            popularity = it.popularity,
            numberOfSeasons = it.numberOfSeasons,
            voteCount = it.voteCount,
            firstAirDate = it.firstAirDate,
            overview = it.overview,
            posterPath = it.posterPath,
            originalName = it.originalName,
            voteAverage = it.voteAverage,
            name = it.name,
            tagline = it.tagline,
            inProduction = it.inProduction,
            lastAirDate = it.lastAirDate,
            homepage = it.homepage,
            status = it.status
        )
    }

private fun domainGenreToEntityRoom(from: List<Genre>): List<Genres> =
    from.map {
        Genres(it.genreId, it.genreName)
    }

internal fun domainProductCompaniesToEntityRoom(from: List<ProductionCompany>): List<ProductionCompanies> =
    from.map {
        ProductionCompanies(it.productionId, it.logoPath, it.name, it.originCountry)
    }

private fun domainCreatorsToEntityRoom(from: List<Creator>, ownerId: Int): List<CreatedByEntity> =
    from.map {
        CreatedByEntity(
            id = it.id,
            ownerId = ownerId,
            gender = it.gender,
            creditId = it.creditId,
            name = it.name,
            profilePath = it.profilePath,
        )
    }

private fun concatEpisodeToAirFromDomain(
    lastEpisodeToAir: EpisodeToAir?,
    nextEpisodeToAir: EpisodeToAir?,
    ownerId: Int
) =
    mutableListOf<EpisodesToAir>().apply {
        fun cvt(e: EpisodeToAir, airingType: EpisodesToAir.AiringType) = e.let {
            EpisodesToAir(
                id = it.id,
                productionCode = it.productionCode,
                airDate = it.airDate,
                overview = it.overview,
                episodeNumber = it.episodeNumber,
                voteAverage = it.voteAverage,
                name = it.name,
                seasonNumber = it.seasonNumber,
                stillPath = it.stillPath,
                voteCount = it.voteCount,
                ownerId = ownerId,
                airingType = airingType
            )
        }
        lastEpisodeToAir?.let { add(cvt(it, EpisodesToAir.AiringType.LAST_AIR)) }
        nextEpisodeToAir?.let { add(cvt(it, EpisodesToAir.AiringType.NEXT_AIR)) }
    }

private fun domainTvSeasonToRoomEntity(
    seasons: List<id.apwdevs.app.core.domain.model.detail.TvShowSeason>,
    ownerId: Int
): List<TvShowSeason> =
    seasons.map {
        TvShowSeason(
            it.id,
            ownerId,
            it.airDate,
            it.overview,
            it.episodeCount,
            it.name,
            it.seasonNumber,
            it.posterPath
        )
    }

/**
 * For movie
 */
private fun favDetailMovieEntity(detailMovie: DetailMovie) =
    detailMovie.let {
        FavDetailMovieEntity(
            id = it.movieId,
            originalLanguage = it.originalLanguage,
            title = it.title,
            backdropPath = it.backdropPath,
            revenue = it.revenue,
            popularity = it.popularity,
            voteCount = it.voteCount,
            budget = it.budget,
            overview = it.overview,
            originalTitle = it.originalTitle,
            runtime = it.runtime,
            posterPath = it.posterPath,
            releaseDate = it.releaseDate,
            voteAverage = it.voteAverage,
            tagline = it.tagline,
            adult = it.adult,
            homepage = it.homepage,
            status = it.status,
        )
    }

@JvmName("mapDetailMovieIntoEntity")
fun DetailMovie.mapToEntity(): FavDetailMovie {
    return FavDetailMovie(
        detailMovie = favDetailMovieEntity(this),
        genres = domainGenreToEntityRoom(genres),
        productionCompanies = domainProductCompaniesToEntityRoom(
            productionCompanies
        )
    )
}

@JvmName("mapDetailTvShowIntoEntity")
fun DetailTvShow.mapToEntity(): FavDetailTvShow {
    return FavDetailTvShow(
        favDetailTvShow = extractTvShowEntityFromDomain(this),
        genres = domainGenreToEntityRoom(genres),
        productionCompanies = domainProductCompaniesToEntityRoom(productionCompanies),
        creators = domainCreatorsToEntityRoom(creators, id),
        lastAndNextEpisodeToAir = concatEpisodeToAirFromDomain(
            lastEpisodeToAir,
            nextEpisodeToAir,
            id
        ),
        seasons = domainTvSeasonToRoomEntity(seasons, id)
    )

}