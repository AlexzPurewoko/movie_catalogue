package id.apwdevs.app.detail.di

import id.apwdevs.app.detail.viewmodel.CardBackdropViewModel
import id.apwdevs.app.detail.viewmodel.DetailMovieShowVM
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailModule = module {
    viewModel { DetailMovieShowVM(get(), get(), get()) }
    viewModel { CardBackdropViewModel(get()) }
}