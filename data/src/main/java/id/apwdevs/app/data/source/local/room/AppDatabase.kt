package id.apwdevs.app.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.apwdevs.app.data.source.local.room.dao.*
import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.local.entity.RemoteKeysMovie
import id.apwdevs.app.data.source.local.entity.RemoteKeysTvShow
import id.apwdevs.app.data.source.local.entity.detail.GenreMapper
import id.apwdevs.app.data.source.local.entity.detail.ProductionCompanies
import id.apwdevs.app.data.source.local.entity.detail.ProductionCompaniesMapper
import id.apwdevs.app.data.source.local.entity.detail.movie.FavDetailMovieEntity
import id.apwdevs.app.data.source.local.entity.detail.tvshow.CreatedByEntity
import id.apwdevs.app.data.source.local.entity.detail.tvshow.EpisodesToAir
import id.apwdevs.app.data.source.local.entity.detail.tvshow.FavDetailTvShowEntity
import id.apwdevs.app.data.source.local.entity.detail.tvshow.TvShowSeason
import id.apwdevs.app.data.source.local.entity.items.MovieEntity
import id.apwdevs.app.data.source.local.entity.items.TvEntity

@Database(entities = [

    RemoteKeysMovie::class,
    RemoteKeysTvShow::class,

    Genres::class,
    TvEntity::class,
    MovieEntity::class,

    // helper entities
    GenreMapper::class,
    ProductionCompanies::class,
    ProductionCompaniesMapper::class,
    CreatedByEntity::class,
    EpisodesToAir::class,
    TvShowSeason::class,

    FavDetailMovieEntity::class,
    FavDetailTvShowEntity::class
], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun genreDao(): GenreDao
    abstract fun movieDao(): MovieDao
    abstract fun tvShowDao(): TvShowDao
    abstract fun favMovieDetail(): FavDetailMovieDao
    abstract fun favTvShowDetail(): FavTvShowDao
    abstract fun remoteKeysMovieDao(): RemoteKeysMovieDao
    abstract fun remoteKeysTvShowDao(): RemoteKeysTvShowDao

}