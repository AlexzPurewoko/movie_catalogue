package id.apwdevs.app.movieshow.di

import id.apwdevs.app.core.domain.usecase.*
import org.koin.dsl.module

val useCaseModule = module {
    factory<SearchUseCase> { SearchInteractor(get(), get()) }
    factory<DetailUseCase> { DetailInteractor(get(), get()) }
    factory<FavoriteUseCase> { FavoriteInteractor(get(), get()) }
    factory<DiscoverPopularUseCase> { DiscoverPopularInteractor(get(), get()) }
}