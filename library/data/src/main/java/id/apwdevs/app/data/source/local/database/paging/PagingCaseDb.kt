package id.apwdevs.app.data.source.local.database.paging

import androidx.paging.PagingSource
import androidx.sqlite.db.SupportSQLiteQuery
import id.apwdevs.app.data.source.local.entity.Genres

interface PagingCaseDb<Data : Any, RemoteKey : Any> {

    // movie
    suspend fun insert(data: List<Data>)

    fun getAllDataPaging(): PagingSource<Int, Data>

    suspend fun getAllData(): List<Data>

    // genre
    suspend fun getGenres(): List<Genres>
    suspend fun insertGenres(genres: List<Genres>)

    // remote keys
    suspend fun insertAllRemoteKey(remoteKey: List<RemoteKey>)

    suspend fun getAllRemoteKey(): List<RemoteKey>

    suspend fun deleteKey(query: SupportSQLiteQuery): Long

    suspend fun remoteKeysId(id: Long): RemoteKey?

    suspend fun clearRemoteKeys()

    suspend fun <R> provideTransaction(block: suspend () -> R): R
}

