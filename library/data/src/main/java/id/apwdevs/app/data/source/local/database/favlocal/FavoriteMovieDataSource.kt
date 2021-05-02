package id.apwdevs.app.data.source.local.database.favlocal

import id.apwdevs.app.data.source.local.entity.detail.movie.FavDetailMovie
import id.apwdevs.app.data.source.local.entity.detail.movie.FavDetailMovieEntity
import id.apwdevs.app.data.source.local.room.AppDatabase

class FavoriteMovieDataSource(
    appDb: AppDatabase
) : FavoriteMovieSource(appDb) {
    private val movieDao = appDatabase.favMovieDetail()

    override suspend fun getAllFavorite(): List<FavDetailMovieEntity> {
        return movieDao.getMovieEntities()
    }

    override suspend fun getFavorite(id: Int): FavDetailMovie {
        return movieDao.getMovie(id)
    }

    override suspend fun isFavorite(id: Int): Boolean {
        return movieDao.isMovieExists(id)
    }

    override suspend fun deleteData(id: Int) {
        movieDao.deleteById(id)
    }

    override suspend fun save(data: FavDetailMovie) {
        movieDao.insertFavDetailMovie(data)
    }
}