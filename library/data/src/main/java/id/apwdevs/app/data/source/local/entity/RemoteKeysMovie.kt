package id.apwdevs.app.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys_movie")
data class RemoteKeysMovie(
    @PrimaryKey @ColumnInfo(name = "movie_id") val movieId: Long,
    @ColumnInfo(name = "previous_key") val prevKey: Int?,
    @ColumnInfo(name = "next_key") val nextKey: Int?
)

