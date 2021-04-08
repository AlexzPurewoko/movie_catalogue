package id.apwdevs.app.core.di

import id.apwdevs.app.core.domain.repository.FavoriteMovieRepository
import id.apwdevs.app.core.domain.repository.FavoriteTvShowRepository
import id.apwdevs.app.core.domain.repository.MovieRepository
import id.apwdevs.app.core.domain.repository.TvShowRepository2
import id.apwdevs.app.core.repository.FavMovieRepositoryImpl
import id.apwdevs.app.core.repository.FavTvShowRepositoryImpl
import id.apwdevs.app.core.repository.MovieRepoImpl
import id.apwdevs.app.core.repository.TvShowRepoImpl
import org.koin.dsl.module

val repoModule = module {
    factory<MovieRepository> { MovieRepoImpl(get(), get()) }
    factory<TvShowRepository2> { TvShowRepoImpl(get(), get()) }

    factory<FavoriteMovieRepository> { FavMovieRepositoryImpl(get()) }
    factory<FavoriteTvShowRepository> { FavTvShowRepositoryImpl(get()) }
}