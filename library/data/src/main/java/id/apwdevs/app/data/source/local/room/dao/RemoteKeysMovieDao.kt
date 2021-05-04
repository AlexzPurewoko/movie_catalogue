package id.apwdevs.app.data.source.local.room.dao

import androidx.annotation.VisibleForTesting
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import id.apwdevs.app.data.source.local.entity.RemoteKeysMovie

@Dao
interface RemoteKeysMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeysMovie>)

    @VisibleForTesting
    @Query("SELECT * FROM remote_keys_movie")
    suspend fun getAll(): List<RemoteKeysMovie>

    @RawQuery
    suspend fun deleteKeys(query: SupportSQLiteQuery): Long

    @Query("SELECT * FROM remote_keys_movie WHERE movie_id=:movieId")
    suspend fun remoteKeysMovieId(movieId: Long): RemoteKeysMovie?

    @Query("DELETE FROM remote_keys_movie")
    suspend fun clearRemoteKeys()
}

