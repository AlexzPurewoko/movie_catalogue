package id.apwdevs.app.core.di

import id.apwdevs.app.core.domain.repository.*
import id.apwdevs.app.core.domain.usecase.*
import id.apwdevs.app.core.repository.*
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