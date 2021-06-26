package id.apwdevs.app.data.di

import id.apwdevs.app.data.source.remote.RemoteInit
import id.apwdevs.app.data.source.remote.network.MoviesNetwork
import id.apwdevs.app.data.source.remote.network.MoviesNetworkImpl
import id.apwdevs.app.data.source.remote.network.TvShowsNetwork
import id.apwdevs.app.data.source.remote.network.TvShowsNetworkImpl
import org.koin.dsl.module

val networkModule = module {
    single { RemoteInit.init() }
}

val netWorkAccessModule = module {
    factory<MoviesNetwork> { MoviesNetworkImpl(get()) }
    factory<TvShowsNetwork> { TvShowsNetworkImpl(get()) }
}