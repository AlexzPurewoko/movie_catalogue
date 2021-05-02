package id.apwdevs.app.detail.di

import id.apwdevs.app.detail.viewmodel.CardBackdropViewModel
import id.apwdevs.app.detail.viewmodel.DetailMovieShowVM
import id.apwdevs.app.detail.viewmodel.DetailViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailModule = module {
    viewModel { DetailViewModel(get(), get(), get()) }
    viewModel { DetailMovieShowVM(get(), get(), get()) }
    viewModel { CardBackdropViewModel(get()) }
}