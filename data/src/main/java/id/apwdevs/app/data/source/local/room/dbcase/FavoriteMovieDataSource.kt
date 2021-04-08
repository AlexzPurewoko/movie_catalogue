package id.apwdevs.app.data.source.local.room.dbcase

import id.apwdevs.app.data.source.local.entity.detail.movie.FavDetailMovie
import id.apwdevs.app.data.source.local.entity.detail.movie.FavDetailMovieEntity
import id.apwdevs.app.data.source.local.room.AppDatabase

class FavoriteMovieDataSource(
        appDb: AppDatabase
) : FavoriteDataSource<FavDetailMovieEntity, FavDetailMovie>(appDb) {
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

    override suspend fun toggleFavorite(data: FavDetailMovie, favorited: Boolean) {
        if (favorited)
            movieDao.insertFavDetailMovie(data)
        else
            movieDao.deleteById(data.detailMovie.id)
    }

}