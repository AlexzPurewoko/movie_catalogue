package id.apwdevs.app.core.utils

import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.local.entity.detail.ProductionCompanies
import id.apwdevs.app.data.source.local.entity.detail.movie.FavDetailMovie
import id.apwdevs.app.data.source.local.entity.detail.tvshow.CreatedByEntity
import id.apwdevs.app.data.source.local.entity.detail.tvshow.EpisodesToAir
import id.apwdevs.app.data.source.local.entity.detail.tvshow.FavDetailTvShow
import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.Genre
import id.apwdevs.app.core.domain.model.detail.Creator
import id.apwdevs.app.core.domain.model.detail.EpisodeToAir
import id.apwdevs.app.core.domain.model.detail.ProductionCompany
import id.apwdevs.app.core.domain.model.detail.TvShowSeason

object EntityToDomainMapper {

    fun genres(from: List<Genres>) : List<Genre> =
        from.map {
            Genre(it.id, it.genreName)
        }

    fun productionCompanies(from: List<ProductionCompanies>): List<ProductionCompany> =
        from.map {
            ProductionCompany(it.productionId, it.logoPath, it.name, it.originCountry)
        }

    fun creators(from: List<CreatedByEntity>): List<Creator> =
        from.map {
            Creator(
                id = it.id,
                gender = it.gender,
                creditId = it.creditId,
                name = it.name,
                profilePath = it.profilePath,
            )
        }

    fun seasons(from: List<id.apwdevs.app.data.source.local.entity.detail.tvshow.TvShowSeason>): List<TvShowSeason> =
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
    fun episodeToAir(from: EpisodesToAir?) =
        from?.let {
            EpisodeToAir(
                id = it.id, productionCode = it.productionCode, airDate = it.airDate, overview = it.overview,
                episodeNumber = it.episodeNumber, voteAverage = it.voteAverage, name = it.name, seasonNumber = it.seasonNumber,
                stillPath = it.stillPath, voteCount = it.voteCount
            )
        }
    fun detailTvShow(detail: FavDetailTvShow): DetailTvShow =
        detail.let {
            val entity = it.favDetailTvShow
            val genre = genres(it.genres)
            val creator = creators(it.creators)
            it.lastAndNextEpisodeToAir
            val lastEpisodeToAir = detail.lastAndNextEpisodeToAir.find { i -> i.airingType == EpisodesToAir.AiringType.LAST_AIR }
            val nextEpisodeToAir = detail.lastAndNextEpisodeToAir.find { i -> i.airingType == EpisodesToAir.AiringType.NEXT_AIR }
            val productionCompany = productionCompanies(it.productionCompanies)
            val season = seasons(it.seasons)
            DetailTvShow(
                id = entity.id, originalLanguage = entity.originalLanguage, numberOfEpisodes = entity.numberOfEpisodes, type = entity.type, backdropPath = entity.backdropPath, popularity = entity.popularity,
                numberOfSeasons = entity.numberOfSeasons, voteCount = entity.voteCount, firstAirDate = entity.firstAirDate, overview = entity.overview, posterPath = entity.posterPath,
                originalName = entity.originalName, voteAverage = entity.voteAverage, name = entity.name, tagline = entity.tagline,
                inProduction = entity.inProduction, lastAirDate = entity.lastAirDate, homepage = entity.homepage, status = entity.status, genres = genre,
                productionCompanies = productionCompany, creators = creator, lastEpisodeToAir = episodeToAir(lastEpisodeToAir)!!, nextEpisodeToAir = episodeToAir(nextEpisodeToAir), seasons = season
            )
        }

    /**
     * Movie
     */
    fun detailMovie(detailEntity: FavDetailMovie): DetailMovie =
        detailEntity.let {
            val i = it.detailMovie
            DetailMovie(
                movieId = i.id, originalLanguage = i.originalLanguage, title = i.title, backdropPath = i.backdropPath, revenue = i.revenue, popularity = i.popularity,
                voteCount = i.voteCount, budget = i.budget, overview = i.overview, originalTitle = i.originalTitle,
                runtime = i.runtime, posterPath = i.posterPath, releaseDate = i.releaseDate, voteAverage = i.voteAverage, tagline = i.tagline,
                adult = i.adult, homepage = i.homepage, status = i.status, genres = genres(it.genres), productionCompanies = productionCompanies(it.productionCompanies)
            )

        }
}