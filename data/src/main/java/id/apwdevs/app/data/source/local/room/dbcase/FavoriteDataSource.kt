package id.apwdevs.app.data.source.local.room.dbcase

import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.local.room.AppDatabase

abstract class FavoriteDataSource<Entity, DetailEntity>(protected val appDatabase: AppDatabase) {
    abstract suspend fun getAllFavorite(): List<Entity>
    abstract suspend fun getFavorite(id: Int): DetailEntity
    abstract suspend fun isFavorite(id: Int): Boolean
    abstract suspend fun toggleFavorite(data: DetailEntity, favorited: Boolean)

    suspend fun genres(): List<Genres> {
        return appDatabase.genreDao().getAllGenres()
    }
}

