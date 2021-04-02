package id.apwdevs.app.core.utils

import id.apwdevs.app.data.source.remote.response.moviedetail.MovieDetailResponse
import id.apwdevs.app.data.source.remote.response.tvdetail.TvDetailResponse
import id.apwdevs.app.data.source.remote.response.tvdetail.parts.CreatedByItem
import id.apwdevs.app.data.source.remote.response.tvdetail.parts.SeasonsItem
import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.Genre
import id.apwdevs.app.core.domain.model.detail.Creator
import id.apwdevs.app.core.domain.model.detail.EpisodeToAir
import id.apwdevs.app.core.domain.model.detail.ProductionCompany
import id.apwdevs.app.core.domain.model.detail.TvShowSeason

object RemoteToDomainMapper {
    fun genres(from: List<id.apwdevs.app.data.source.remote.response.parts.Genre>): List<Genre> =
        from.map { genre ->
            Genre(genre.id, genre.name)
        }

    fun productionCompanies(from: List<id.apwdevs.app.data.source.remote.response.parts.ProductionCompany>): List<ProductionCompany> =
        from.map { item ->
            ProductionCompany(item.id, item.logoPath, item.name, item.originCountry)
        }

    fun creators(from: List<CreatedByItem>): List<Creator> =
        from.map {
            Creator(it.id, it.gender, it.creditId, it.name, it.profilePath)
        }

    fun episodeToAir(from: id.apwdevs.app.data.source.remote.response.tvdetail.parts.EpisodesToAir?): EpisodeToAir? =
        from?.let {
            EpisodeToAir(
                id = it.id, productionCode = it.productionCode, airDate = it.airDate, overview = it.overview,
                episodeNumber = it.episodeNumber, voteAverage = it.voteAverage, name = it.name, seasonNumber = it.seasonNumber,
                stillPath = it.stillPath, voteCount = it.voteCount
            )
        }

    fun tvSeasons(from: List<SeasonsItem>): List<TvShowSeason> =
        from.map {
            TvShowSeason(it.id, it.airDate, it.overview, it.episodeCount, it.name, it.seasonNumber, it.posterPath)
        }

    fun detailTvShow(tvDetailResponse: TvDetailResponse) =
        tvDetailResponse.let {
            val genres = genres(it.genres)
            val productionCompanies = productionCompanies(it.productionCompanies)
            val creators = creators(it.createdBy)
            val lastEpisode = episodeToAir(it.lastEpisodeToAir)
            val nextEpisode = episodeToAir(it.nextEpisodeToAir)
            val tvShowSeason = tvSeasons(it.seasons)
            DetailTvShow(
                id = it.id, originalLanguage = it.originalLanguage, numberOfEpisodes = it.numberOfEpisodes , type = it.type,
                backdropPath = it.backdropPath, popularity = it.popularity, numberOfSeasons = it.numberOfSeasons, voteCount = it.voteCount , firstAirDate = it.firstAirDate,
                overview = it.overview, posterPath = it.posterPath, originalName = it.originalName, voteAverage = it.voteAverage, name = it.name,
                tagline = it.tagline, inProduction = it.inProduction, lastAirDate = it.lastAirDate, homepage = it.homepage, status = it.status, genres = genres,
                productionCompanies = productionCompanies, creators = creators, lastEpisodeToAir = lastEpisode!!, nextEpisodeToAir = nextEpisode, seasons = tvShowSeason
            )
        }

    fun detailMovie(movieDetailResponse: MovieDetailResponse) =
        movieDetailResponse.let {
            val genres = genres(it.genres)
            val productionCompanies = productionCompanies(it.productionCompanies)
            DetailMovie(
                movieId = it.id, originalLanguage = it.originalLanguage, title = it.title, backdropPath = it.backdropPath,
                revenue = it.revenue, popularity = it.popularity, voteCount = it.voteCount, budget = it.budget, overview = it.overview,
                originalTitle = it.originalTitle, runtime = it.runtime, posterPath = it.posterPath, releaseDate = it.releaseDate,
                voteAverage = it.voteAverage, tagline = it.tagline, adult = it.adult, homepage = it.homepage, status = it.status,
                genres = genres, productionCompanies = productionCompanies
            )
        }
}