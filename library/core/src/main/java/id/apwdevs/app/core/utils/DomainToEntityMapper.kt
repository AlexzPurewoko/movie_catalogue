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


object DomainToEntityMapper {

    /**
     * Domain to Entity mapper
     */

    fun extractTvShowEntityFromDomain(from: DetailTvShow): FavDetailTvShowEntity =
        from.let {
            FavDetailTvShowEntity(
                id = it.id, originalLanguage = it.originalLanguage, numberOfEpisodes = it.numberOfEpisodes, type = it.type,
                backdropPath = it.backdropPath, popularity = it.popularity, numberOfSeasons = it.numberOfSeasons, voteCount = it.voteCount, firstAirDate = it.firstAirDate,
                overview = it.overview, posterPath = it.posterPath, originalName = it.originalName, voteAverage = it.voteAverage, name = it.name,
                tagline = it.tagline, inProduction = it.inProduction, lastAirDate = it.lastAirDate, homepage = it.homepage, status = it.status
            )
        }

    fun domainGenreToEntityRoom(from: List<Genre>): List<Genres> =
        from.map {
            Genres(it.genreId, it.genreName)
        }

    fun domainProductCompaniesToEntityRoom(from: List<ProductionCompany>): List<ProductionCompanies> =
        from.map {
            ProductionCompanies(it.productionId, it.logoPath, it.name, it.originCountry)
        }
    fun domainCreatorsToEntityRoom(from: List<Creator>, ownerId: Int): List<CreatedByEntity> =
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
    fun concatEpisodeToAirFromDomain(lastEpisodeToAir: EpisodeToAir?, nextEpisodeToAir: EpisodeToAir?, ownerId: Int) =
        mutableListOf<EpisodesToAir>().apply {
            fun cvt(e: EpisodeToAir, airingType: EpisodesToAir.AiringType) = e.let {
                EpisodesToAir(
                    id = it.id, productionCode = it.productionCode, airDate = it.airDate, overview = it.overview,
                    episodeNumber = it.episodeNumber, voteAverage = it.voteAverage, name = it.name, seasonNumber = it.seasonNumber,
                    stillPath = it.stillPath, voteCount = it.voteCount, ownerId = ownerId, airingType = airingType
                )
            }
            lastEpisodeToAir?.let { add(cvt(it, EpisodesToAir.AiringType.LAST_AIR)) }
            nextEpisodeToAir?.let { add(cvt(it, EpisodesToAir.AiringType.NEXT_AIR)) }
        }

    fun domainTvSeasonToRoomEntity(seasons: List<id.apwdevs.app.core.domain.model.detail.TvShowSeason>, ownerId: Int): List<TvShowSeason> =
        seasons.map {
            TvShowSeason(it.id, ownerId, it.airDate, it.overview, it.episodeCount, it.name, it.seasonNumber, it.posterPath)
        }

    fun favDetailTvShow(detailTvShow: DetailTvShow) =
        detailTvShow.let {
            val detailEntity = extractTvShowEntityFromDomain(it)
            val genres = domainGenreToEntityRoom(it.genres)
            val productionCompanies = domainProductCompaniesToEntityRoom(it.productionCompanies)
            val creators = domainCreatorsToEntityRoom(it.creators, it.id)
            val concatedEpisodesToAir = concatEpisodeToAirFromDomain(it.lastEpisodeToAir, it.nextEpisodeToAir, it.id)
            val tvSeasons = domainTvSeasonToRoomEntity(it.seasons, it.id)
            FavDetailTvShow(
                favDetailTvShow = detailEntity,
                genres = genres,
                productionCompanies = productionCompanies,
                creators = creators,
                lastAndNextEpisodeToAir = concatedEpisodesToAir,
                seasons = tvSeasons
            )
        }


    /**
     * For movie
     */
    fun favDetailMovieEntity(detailMovie: DetailMovie) =
        detailMovie.let {
            FavDetailMovieEntity(
                id = it.movieId, originalLanguage = it.originalLanguage, title = it.title, backdropPath = it.backdropPath, revenue = it.revenue, popularity = it.popularity,
                voteCount = it.voteCount, budget = it.budget, overview = it.overview, originalTitle = it.originalTitle,
                runtime = it.runtime, posterPath = it.posterPath, releaseDate = it.releaseDate, voteAverage = it.voteAverage, tagline = it.tagline,
                adult = it.adult, homepage = it.homepage, status = it.status, 
            )
        }

    fun favDetailMovie(detailMovie: DetailMovie) =
        detailMovie.let {
            FavDetailMovie(
                detailMovie = favDetailMovieEntity(it),
                genres = domainGenreToEntityRoom(detailMovie.genres),
                productionCompanies = domainProductCompaniesToEntityRoom(detailMovie.productionCompanies)
            )
        }
}