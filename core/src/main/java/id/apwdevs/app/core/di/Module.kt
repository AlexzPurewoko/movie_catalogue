package id.apwdevs.app.core.di

import id.apwdevs.app.core.repository.FavTvShowRepository
import id.apwdevs.app.core.domain.repository.IFavoriteMovieRepository
import id.apwdevs.app.core.domain.repository.IFavoriteTvShowRepository
import id.apwdevs.app.core.domain.repository.IMoviesRepository
import id.apwdevs.app.core.domain.repository.ITvShowRepository
import id.apwdevs.app.core.repository.FavMovieRepository
import id.apwdevs.app.core.repository.MovieRepository
import id.apwdevs.app.core.repository.TvShowRepository
import org.koin.dsl.module

val repoModule = module {
    factory<IMoviesRepository> { MovieRepository(get(), get()) }
    factory<ITvShowRepository> { TvShowRepository(get(), get()) }
    factory<IFavoriteMovieRepository> { FavMovieRepository(get()) }
    factory<IFavoriteTvShowRepository> { FavTvShowRepository(get()) }
}