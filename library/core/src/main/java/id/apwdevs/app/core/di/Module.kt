package id.apwdevs.app.core.di

import id.apwdevs.app.core.domain.repository.FavMovieRepository
import id.apwdevs.app.core.domain.repository.FavTvShowRepository
import id.apwdevs.app.core.domain.repository.MovieRepository
import id.apwdevs.app.core.domain.repository.TvShowRepository
import id.apwdevs.app.core.domain.usecase.*
import id.apwdevs.app.core.repository.FavoriteMovieRepoImpl
import id.apwdevs.app.core.repository.FavoriteTvShowRepoImpl
import id.apwdevs.app.core.repository.MovieRepoImpl
import id.apwdevs.app.core.repository.TvShowRepoImpl
import org.koin.dsl.module

val useCaseModule = module {
    factory<SearchUseCase> { SearchInteractor(get(), get()) }
    factory<DetailUseCase> { DetailInteractor(get(), get()) }

    factory<FavUseCase> { FavInteractor(get(), get()) }
    factory<DiscoverPopularUseCase> { DiscoverPopularInteractor(get(), get()) }

}

val repoModule = module {
    factory<MovieRepository> { MovieRepoImpl(get(), get()) }
    factory<TvShowRepository> { TvShowRepoImpl(get(), get()) }

    factory<FavMovieRepository> { FavoriteMovieRepoImpl(get()) }
    factory<FavTvShowRepository> { FavoriteTvShowRepoImpl(get()) }

}