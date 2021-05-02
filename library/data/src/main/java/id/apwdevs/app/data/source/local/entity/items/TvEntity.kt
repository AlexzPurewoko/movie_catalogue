package id.apwdevs.app.data.source.local.entity.items

import androidx.room.*
import id.apwdevs.app.data.source.local.entity.RemoteKeysTvShow
import id.apwdevs.app.data.source.local.entity.converters.GenreIdsTypeConverter

@Entity(
        tableName = "tvshows",
        foreignKeys = [
            ForeignKey(
                    entity = RemoteKeysTvShow::class,
                    parentColumns = ["tv_id"],
                    childColumns = ["id"],
                    onDelete = ForeignKey.CASCADE
            )
        ]
)
@TypeConverters(GenreIdsTypeConverter::class)
data class TvEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "first_air_date") val firstAirDate: String,
    @ColumnInfo(name = "overview") val overview: String,
    @ColumnInfo(name = "original_language") val language: String,
    @ColumnInfo(name = "genre_ids") val genreIds: GenreIdsTypeConverter.GenreIdData,
    @ColumnInfo(name = "poster_path") val posterPath: String?,
    @ColumnInfo(name = "backdrop_path") val backdropPath: String?,
    @ColumnInfo(name = "vote_average") val voteAverage: Double,
    @ColumnInfo(name = "vote_count") val voteCount: Int,
    @ColumnInfo(name = "page_at") val page: Int
)

