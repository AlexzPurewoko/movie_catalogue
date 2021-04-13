package id.apwdevs.app.data.source.local.room.dbcase.favlocal

import id.apwdevs.app.data.source.local.entity.detail.tvshow.FavDetailTvShow
import id.apwdevs.app.data.source.local.entity.detail.tvshow.FavDetailTvShowEntity
import id.apwdevs.app.data.source.local.room.AppDatabase

class FavoriteTvShowDataSource(
    appDb: AppDatabase
) : FavoriteTvShowSource(appDb) {
    private val movieDao = appDatabase.favTvShowDetail()

    override suspend fun getAllFavorite(): List<FavDetailTvShowEntity> {
        return movieDao.getTvShowEntities()
    }

    override suspend fun getFavorite(id: Int): FavDetailTvShow {
        return movieDao.getTvShow(id)
    }

    override suspend fun isFavorite(id: Int): Boolean {
        return movieDao.isTvShowExists(id)
    }

    override suspend fun toggleFavorite(data: FavDetailTvShow, favorited: Boolean) {
        if (favorited)
            movieDao.insertFavDetailTvShow(data)
        else
            movieDao.deleteById(data.favDetailTvShow.id)
    }

    override suspend fun save(data: FavDetailTvShow) {
        movieDao.insertFavDetailTvShow(data)
    }

    override suspend fun deleteData(id: Int) {
        movieDao.deleteById(id)
    }

}