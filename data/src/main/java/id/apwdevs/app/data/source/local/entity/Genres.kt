package id.apwdevs.app.data.source.local.entity

import androidx.room.*

@Entity(
    tableName = "genres"
)
data class Genres(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "genre_id") val id: Int,
    @ColumnInfo(name = "name") val genreName: String
)

