package id.apwdevs.app.search.di

import id.apwdevs.app.search.ui.SearchVewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    viewModel { SearchVewModel(get()) }
}