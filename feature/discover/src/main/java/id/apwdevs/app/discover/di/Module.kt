package id.apwdevs.app.discover.di

import id.apwdevs.app.discover.ui.child.MovieShowViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val discoverViewModel = module {
    viewModel { MovieShowViewModel(get(), get()) }
}