package id.apwdevs.app.data.source.local.entity.detail

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "genre_mapper",
    primaryKeys = ["id", "genre_id"]//,
//    foreignKeys = [
//
//        // for fav detail movie
//        ForeignKey(
//            entity = FavDetailMovieEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["genre_id"],
//            onDelete = CASCADE,
//            onUpdate = CASCADE
//        ),
//
//        // for fav detail tvshows
//        ForeignKey(
//                entity = FavDetailTvShowEntity::class,
//                parentColumns = ["id"],
//                childColumns = ["genre_id"],
//                onDelete = CASCADE,
//                onUpdate = CASCADE
//        )
//    ]
)
data class GenreMapper(
    @ColumnInfo(name = "id") val idMovieShow: Int,
    @ColumnInfo(name = "genre_id") val genreId: Int
)