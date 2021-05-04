package id.apwdevs.app.data.source.local.entity.detail

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "genre_mapper",
    primaryKeys = ["id", "genre_id"]
)
data class GenreMapper(
    @ColumnInfo(name = "id") val idMovieShow: Int,
    @ColumnInfo(name = "genre_id") val genreId: Int
)