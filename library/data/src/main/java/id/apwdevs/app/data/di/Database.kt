package id.apwdevs.app.data.di

import id.apwdevs.app.data.source.local.entity.detail.movie.FavDetailMovie
import id.apwdevs.app.data.source.local.entity.detail.movie.FavDetailMovieEntity
import id.apwdevs.app.data.source.local.entity.detail.tvshow.FavDetailTvShow
import id.apwdevs.app.data.source.local.entity.detail.tvshow.FavDetailTvShowEntity
import id.apwdevs.app.data.source.local.room.dbcase.FavoriteDataSource
import id.apwdevs.app.data.source.local.room.dbcase.FavoriteMovieDataSource
import id.apwdevs.app.data.source.local.room.dbcase.FavoriteTvShowDataSource
import id.apwdevs.app.data.source.local.room.dbcase.paging.PagingCaseMovieDb
import id.apwdevs.app.data.source.local.room.dbcase.paging.PagingCaseTvShowDb
import id.apwdevs.app.data.source.local.room.dbcase.paging.PagingMovieCaseDbInteractor
import id.apwdevs.app.data.source.local.room.dbcase.paging.PagingTvShowCaseDbInteractor
import org.koin.dsl.module

val dbAccessModule = module {
    factory<PagingCaseTvShowDb> { PagingTvShowCaseDbInteractor(get()) }
    factory<PagingCaseMovieDb> { PagingMovieCaseDbInteractor(get()) }

    factory<FavoriteDataSource<FavDetailMovieEntity, FavDetailMovie>> { FavoriteMovieDataSource(get()) }
    factory<FavoriteDataSource<FavDetailTvShowEntity, FavDetailTvShow>> {
        FavoriteTvShowDataSource(
            get()
        )
    }
}