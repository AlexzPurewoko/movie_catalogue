package id.apwdevs.app.data.source.local.database.favlocal

import id.apwdevs.app.data.source.local.entity.detail.tvshow.FavDetailTvShow
import id.apwdevs.app.data.source.local.entity.detail.tvshow.FavDetailTvShowEntity
import id.apwdevs.app.data.source.local.room.AppDatabase

abstract class FavoriteTvShowSource(appDatabase: AppDatabase) :
    FavoriteDataSource<FavDetailTvShowEntity, FavDetailTvShow>(appDatabase)