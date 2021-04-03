package id.apwdevs.app.data.di

import id.apwdevs.app.data.source.local.entity.RemoteKeysMovie
import id.apwdevs.app.data.source.local.entity.RemoteKeysTvShow
import id.apwdevs.app.data.source.local.entity.items.MovieEntity
import id.apwdevs.app.data.source.local.entity.items.TvEntity
import id.apwdevs.app.data.source.local.room.AppDatabase
import id.apwdevs.app.data.source.local.room.dbcase.PagingCaseDb
import id.apwdevs.app.data.source.local.room.dbcase.PagingMovieCaseDbInteractor
import id.apwdevs.app.data.source.local.room.dbcase.PagingTvShowCaseDbInteractor
import org.koin.dsl.module

val dbAccessModule = module {
    factory { get<AppDatabase>().genreDao() }
    factory { get<AppDatabase>().movieDao() }
    factory { get<AppDatabase>().tvShowDao() }
    factory { get<AppDatabase>().remoteKeysTvShowDao() }
    factory { get<AppDatabase>().remoteKeysMovieDao() }
    factory { get<AppDatabase>().favMovieDetail() }
    factory { get<AppDatabase>().favTvShowDetail() }

    factory<PagingCaseDb<TvEntity, RemoteKeysTvShow>> { PagingTvShowCaseDbInteractor(get(), get()) }
    factory<PagingCaseDb<MovieEntity, RemoteKeysMovie>> { PagingMovieCaseDbInteractor(get(), get()) }
}