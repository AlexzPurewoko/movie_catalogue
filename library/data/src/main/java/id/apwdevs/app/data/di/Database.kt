package id.apwdevs.app.data.di

import id.apwdevs.app.data.source.local.room.dbcase.favlocal.FavoriteMovieDataSource
import id.apwdevs.app.data.source.local.room.dbcase.favlocal.FavoriteMovieSource
import id.apwdevs.app.data.source.local.room.dbcase.favlocal.FavoriteTvShowDataSource
import id.apwdevs.app.data.source.local.room.dbcase.favlocal.FavoriteTvShowSource
import id.apwdevs.app.data.source.local.room.dbcase.paging.PagingCaseMovieDb
import id.apwdevs.app.data.source.local.room.dbcase.paging.PagingCaseTvShowDb
import id.apwdevs.app.data.source.local.room.dbcase.paging.PagingMovieCaseDbInteractor
import id.apwdevs.app.data.source.local.room.dbcase.paging.PagingTvShowCaseDbInteractor
import org.koin.dsl.module

val dbAccessModule = module {
    factory<PagingCaseTvShowDb> { PagingTvShowCaseDbInteractor(get()) }
    factory<PagingCaseMovieDb> { PagingMovieCaseDbInteractor(get()) }

    factory<FavoriteMovieSource> { FavoriteMovieDataSource(get()) }
    factory<FavoriteTvShowSource> { FavoriteTvShowDataSource(get()) }
}