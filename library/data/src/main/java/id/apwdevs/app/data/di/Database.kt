package id.apwdevs.app.data.di

import androidx.room.Room
import id.apwdevs.app.data.source.local.database.favlocal.FavoriteMovieDataSource
import id.apwdevs.app.data.source.local.database.favlocal.FavoriteMovieSource
import id.apwdevs.app.data.source.local.database.favlocal.FavoriteTvShowDataSource
import id.apwdevs.app.data.source.local.database.favlocal.FavoriteTvShowSource
import id.apwdevs.app.data.source.local.database.paging.PagingCaseMovieDb
import id.apwdevs.app.data.source.local.database.paging.PagingCaseTvShowDb
import id.apwdevs.app.data.source.local.database.paging.PagingMovieCaseDbInteractor
import id.apwdevs.app.data.source.local.database.paging.PagingTvShowCaseDbInteractor
import id.apwdevs.app.data.source.local.room.AppDatabase
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
                get(), AppDatabase::class.java, "app_database.db"
        ).build()
    }
}

val dbAccessModule = module {
    factory<PagingCaseTvShowDb> { PagingTvShowCaseDbInteractor(get()) }
    factory<PagingCaseMovieDb> { PagingMovieCaseDbInteractor(get()) }

    factory<FavoriteMovieSource> { FavoriteMovieDataSource(get()) }
    factory<FavoriteTvShowSource> { FavoriteTvShowDataSource(get()) }
}