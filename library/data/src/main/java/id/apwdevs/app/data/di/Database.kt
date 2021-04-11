package id.apwdevs.app.data.di

import id.apwdevs.app.data.source.local.entity.RemoteKeysMovie
import id.apwdevs.app.data.source.local.entity.RemoteKeysTvShow
import id.apwdevs.app.data.source.local.entity.detail.movie.FavDetailMovie
import id.apwdevs.app.data.source.local.entity.detail.movie.FavDetailMovieEntity
import id.apwdevs.app.data.source.local.entity.detail.tvshow.FavDetailTvShow
import id.apwdevs.app.data.source.local.entity.detail.tvshow.FavDetailTvShowEntity
import id.apwdevs.app.data.source.local.entity.items.MovieEntity
import id.apwdevs.app.data.source.local.entity.items.TvEntity
import id.apwdevs.app.data.source.local.room.dbcase.*
import org.koin.dsl.module

val dbAccessModule = module {
    factory<PagingCaseDb<TvEntity, RemoteKeysTvShow>> { PagingTvShowCaseDbInteractor(get()) }
    factory<PagingCaseDb<MovieEntity, RemoteKeysMovie>> { PagingMovieCaseDbInteractor(get()) }

    factory<FavoriteDataSource<FavDetailMovieEntity, FavDetailMovie>> { FavoriteMovieDataSource(get()) }
    factory<FavoriteDataSource<FavDetailTvShowEntity, FavDetailTvShow>> { FavoriteTvShowDataSource(get()) }
}