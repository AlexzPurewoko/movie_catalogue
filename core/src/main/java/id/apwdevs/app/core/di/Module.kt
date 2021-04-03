package id.apwdevs.app.core.di

import id.apwdevs.app.core.domain.repository.FavoriteMovieRepository
import id.apwdevs.app.core.domain.repository.FavoriteTvShowRepository
import id.apwdevs.app.core.domain.repository.MoviesRepository
import id.apwdevs.app.core.domain.repository.TvShowRepository
import id.apwdevs.app.core.repository.FavMovieRepositoryImpl
import id.apwdevs.app.core.repository.FavTvShowRepositoryImpl
import id.apwdevs.app.core.repository.MovieRepositoryImpl
import id.apwdevs.app.core.repository.TvShowRepositoryImpl
import org.koin.dsl.module

val repoModule = module {
    factory<MoviesRepository> { MovieRepositoryImpl(get(), get()) }
    factory<TvShowRepository> { TvShowRepositoryImpl(get(), get()) }
    factory<FavoriteMovieRepository> { FavMovieRepositoryImpl(get()) }
    factory<FavoriteTvShowRepository> { FavTvShowRepositoryImpl(get()) }
}