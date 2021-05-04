package id.apwdevs.app.data.source.local.entity.items

import androidx.room.*
import id.apwdevs.app.data.source.local.entity.RemoteKeysMovie
import id.apwdevs.app.data.source.local.entity.converters.GenreIdsTypeConverter

@Entity(
    tableName = "movies",
    foreignKeys = [
        ForeignKey(
            entity = RemoteKeysMovie::class,
            parentColumns = ["movie_id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@TypeConverters(GenreIdsTypeConverter::class)
data class MovieEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "overview") val overview: String,
    @ColumnInfo(name = "original_language") val language: String,
    @ColumnInfo(name = "genre_ids") val genreIds: GenreIdsTypeConverter.GenreIdData,
    @ColumnInfo(name = "poster_path") val posterPath: String?,
    @ColumnInfo(name = "backdrop_path") val backdropPath: String?,
    @ColumnInfo(name = "release_date") val releaseDate: String?,
    @ColumnInfo(name = "vote_average") val voteAverage: Double,
    @ColumnInfo(name = "vote_count") val voteCount: Int,
    @ColumnInfo(name = "is_adult") val adult: Boolean,
    @ColumnInfo(name = "page_at") val page: Int
)