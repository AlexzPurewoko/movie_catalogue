package id.apwdevs.app.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.apwdevs.app.data.source.local.entity.RemoteKeysMovie

@Dao
interface RemoteKeysMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeysMovie>)

    @Query("SELECT * FROM remote_keys_movie WHERE movie_id=:movieId")
    suspend fun remoteKeysMovieId(movieId: Long): RemoteKeysMovie?

    @Query("DELETE FROM remote_keys_movie")
    suspend fun clearRemoteKeys()
}

