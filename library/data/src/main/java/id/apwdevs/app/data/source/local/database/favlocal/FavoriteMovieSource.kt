package id.apwdevs.app.data.source.local.database.favlocal

import id.apwdevs.app.data.source.local.entity.detail.movie.FavDetailMovie
import id.apwdevs.app.data.source.local.entity.detail.movie.FavDetailMovieEntity
import id.apwdevs.app.data.source.local.room.AppDatabase

abstract class FavoriteMovieSource(appDatabase: AppDatabase) :
    FavoriteDataSource<FavDetailMovieEntity, FavDetailMovie>(appDatabase)