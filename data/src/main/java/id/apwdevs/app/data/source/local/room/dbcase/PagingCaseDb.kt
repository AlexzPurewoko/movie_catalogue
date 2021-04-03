package id.apwdevs.app.data.source.local.room.dbcase

import androidx.paging.PagingSource
import androidx.sqlite.db.SupportSQLiteQuery

interface PagingCaseDb<Data: Any, RemoteKey: Any> {
    suspend fun insert(data: List<Data>)

    fun getAllDataPaging(): PagingSource<Int, Data>

    suspend fun getAllData(): List<Data>

    // remote keys
    suspend fun insertAllRemoteKey(remoteKey: List<RemoteKey>)

    suspend fun getAllRemoteKey(): List<RemoteKey>

    suspend fun deleteKey(query: SupportSQLiteQuery) : Long

    suspend fun remoteKeysId(id: Long): RemoteKey?

    suspend fun clearRemoteKeys()
}

