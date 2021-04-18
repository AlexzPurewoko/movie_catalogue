package id.apwdevs.app.favorite.di

import id.apwdevs.app.favorite.viewmodel.FavoriteViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteModule = module {
    viewModel { FavoriteViewModel(get(), get()) }
}