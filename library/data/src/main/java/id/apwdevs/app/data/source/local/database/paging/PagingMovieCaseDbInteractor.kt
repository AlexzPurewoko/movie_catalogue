package id.apwdevs.app.data.source.local.database.paging

import androidx.annotation.VisibleForTesting
import androidx.paging.PagingSource
import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteQuery
import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.local.entity.RemoteKeysMovie
import id.apwdevs.app.data.source.local.entity.items.MovieEntity
import id.apwdevs.app.data.source.local.room.AppDatabase
import id.apwdevs.app.data.source.local.room.dao.GenreDao
import id.apwdevs.app.data.source.local.room.dao.MovieDao
import id.apwdevs.app.data.source.local.room.dao.RemoteKeysMovieDao

class PagingMovieCaseDbInteractor(private val db: AppDatabase) : PagingCaseMovieDb {


    private val movieDao: MovieDao = db.movieDao()
    private val remoteKeyDao: RemoteKeysMovieDao = db.remoteKeysMovieDao()
    private val genreDao: GenreDao = db.genreDao()

    override suspend fun insert(data: List<MovieEntity>) {
        return movieDao.insertMovie(data)
    }

    override fun getAllDataPaging(): PagingSource<Int, MovieEntity> {
        return movieDao.getAllMovies()
    }

    override suspend fun getAllData(): List<MovieEntity> {
        return movieDao.getAllMovie()
    }

    override suspend fun insertAllRemoteKey(remoteKey: List<RemoteKeysMovie>) {
        return remoteKeyDao.insertAll(remoteKey)
    }

    @VisibleForTesting
    override suspend fun getAllRemoteKey(): List<RemoteKeysMovie> {
        return remoteKeyDao.getAll()
    }

    override suspend fun deleteKey(query: SupportSQLiteQuery): Long {
        return remoteKeyDao.deleteKeys(query)
    }

    override suspend fun remoteKeysId(id: Long): RemoteKeysMovie? {
        return remoteKeyDao.remoteKeysMovieId(id)
    }

    override suspend fun clearRemoteKeys() {
        remoteKeyDao.clearRemoteKeys()
    }

    override suspend fun <R> provideTransaction(block: suspend () -> R): R {
        return db.withTransaction(block)
    }

    override suspend fun getGenres(): List<Genres> {
        return genreDao.getAllGenres()
    }

    override suspend fun insertGenres(genres: List<Genres>) {
        genreDao.insertGenres(genres)
    }

}