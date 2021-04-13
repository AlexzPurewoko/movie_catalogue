package id.apwdevs.app.data.source.local.room.dbcase.favlocal

import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.local.room.AppDatabase

abstract class FavoriteDataSource<Entity, DetailEntity>(protected val appDatabase: AppDatabase) {
    abstract suspend fun getAllFavorite(): List<Entity>
    abstract suspend fun getFavorite(id: Int): DetailEntity
    abstract suspend fun isFavorite(id: Int): Boolean

    @Deprecated("will be deleted")
    abstract suspend fun toggleFavorite(data: DetailEntity, favorited: Boolean)

    abstract suspend fun save(data: DetailEntity)

    abstract suspend fun deleteData(id: Int)

    suspend fun genres(): List<Genres> {
        return appDatabase.genreDao().getAllGenres()
    }

    suspend fun genreMapper(id: Int): List<Int> {
        return appDatabase.genreDao().genreIdsMapper(id)
    }
}

