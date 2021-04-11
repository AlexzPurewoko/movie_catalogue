package id.apwdevs.app.core.di

import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.model.TvShow
import id.apwdevs.app.core.domain.repository.*
import id.apwdevs.app.core.repository.*
import org.koin.dsl.module

val repoModule = module {
    factory<MovieRepository> { MovieRepoImpl(get(), get()) }
    factory<TvShowRepository> { TvShowRepoImpl(get(), get()) }

    factory<FavoriteRepository<TvShow, DetailTvShow>> { FavoriteTvShowRepoImpl(get()) }
    factory<FavoriteRepository<Movies, DetailMovie>> { FavoriteMovieRepoImpl(get()) }

    // deprecated implementation will be deleted later.
    factory<FavoriteMovieRepository> { FavMovieRepositoryImpl(get()) }
    factory<FavoriteTvShowRepository> { FavTvShowRepositoryImpl(get()) }
}