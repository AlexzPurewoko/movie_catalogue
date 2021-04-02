package id.apwdevs.app.data.source.local.entity.items

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import id.apwdevs.app.data.source.local.entity.converters.GenreIdsTypeConverter
import id.apwdevs.app.data.source.local.entity.converters.ListStrCommaConverter

@Entity(tableName = "tvshows")
@TypeConverters(GenreIdsTypeConverter::class, ListStrCommaConverter::class)
data class TvEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "first_air_date") val firstAirDate: String,
    @ColumnInfo(name = "overview") val overview: String,
    @ColumnInfo(name = "original_language") val language: String,
    @ColumnInfo(name = "genre_ids") val genreIds: GenreIdsTypeConverter.GenreIdData,
    @ColumnInfo(name = "poster_path") val posterPath: String,
    @ColumnInfo(name = "origin_country") val originCountry: ListStrCommaConverter.Data,
    @ColumnInfo(name = "backdrop_path") val backdropPath: String,
    @ColumnInfo(name = "vote_average") val voteAverage: Double,
    @ColumnInfo(name = "vote_count") val voteCount: Int
)

