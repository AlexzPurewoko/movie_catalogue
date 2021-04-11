package id.apwdevs.app.core.di

import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.model.TvShow
import id.apwdevs.app.core.domain.repository.*
import id.apwdevs.app.core.domain.usecase.*
import id.apwdevs.app.core.repository.*
import org.koin.dsl.module

val useCaseModule = module {
    factory<SearchUseCase> { SearchInteractor(get(), get()) }
    factory<DetailUseCase> { DetailInteractor(get(), get()) }

    factory<FavUseCase> { FavInteractor(get(), get()) }
    factory<DiscoverPopularUseCase> { DiscoverPopularInteractor(get(), get()) }

    // deprecated! will be deleted
    factory<FavoriteUseCase> { FavoriteInteractor(get(), get()) }
}

val repoModule = module {
    factory<MovieRepository> { MovieRepoImpl(get(), get()) }
    factory<TvShowRepository> { TvShowRepoImpl(get(), get()) }

    factory<FavoriteRepository<TvShow, DetailTvShow>> { FavoriteTvShowRepoImpl(get()) }
    factory<FavoriteRepository<Movies, DetailMovie>> { FavoriteMovieRepoImpl(get()) }

    // deprecated implementation will be deleted later.
    factory<FavoriteMovieRepository> { FavMovieRepositoryImpl(get()) }
    factory<FavoriteTvShowRepository> { FavTvShowRepositoryImpl(get()) }
}