package id.apwdevs.app.data.source.local.room.dbcase

import androidx.paging.PagingSource
import androidx.sqlite.db.SupportSQLiteQuery
import id.apwdevs.app.data.source.local.entity.RemoteKeysTvShow
import id.apwdevs.app.data.source.local.entity.items.TvEntity
import id.apwdevs.app.data.source.local.room.dao.RemoteKeysTvShowDao
import id.apwdevs.app.data.source.local.room.dao.TvShowDao

class PagingTvShowCaseDbInteractor(
        private val tvShowDao: TvShowDao,
        private val remoteKeysTvShowDao: RemoteKeysTvShowDao
): PagingCaseDb<TvEntity, RemoteKeysTvShow> {
    override suspend fun insert(data: List<TvEntity>) {
        return tvShowDao.insertTvShow(data)
    }

    override fun getAllDataPaging(): PagingSource<Int, TvEntity> {
        return tvShowDao.getAllTvShows()
    }

    override suspend fun getAllData(): List<TvEntity> {
        return tvShowDao.getAllTvShow()
    }

    override suspend fun insertAllRemoteKey(remoteKey: List<RemoteKeysTvShow>) {
        return remoteKeysTvShowDao.insertAll(remoteKey)
    }

    override suspend fun getAllRemoteKey(): List<RemoteKeysTvShow> {
        return remoteKeysTvShowDao.getAll()
    }

    override suspend fun deleteKey(query: SupportSQLiteQuery): Long {
        return remoteKeysTvShowDao.deleteKeys(query)
    }

    override suspend fun remoteKeysId(id: Long): RemoteKeysTvShow? {
        return remoteKeysTvShowDao.remoteKeysTvShowId(id)
    }

    override suspend fun clearRemoteKeys() {
        remoteKeysTvShowDao.clearRemoteKeys()
    }

}

