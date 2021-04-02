package id.apwdevs.app.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.apwdevs.app.data.source.local.entity.RemoteKeysTvShow

@Dao
interface RemoteKeysTvShowDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeysTvShow>)

    @Query("SELECT * FROM remote_keys_tvshow WHERE tv_id=:tvId")
    suspend fun remoteKeysTvShowId(tvId: Long): RemoteKeysTvShow?

    @Query("DELETE FROM remote_keys_tvshow")
    suspend fun clearRemoteKeys()
}