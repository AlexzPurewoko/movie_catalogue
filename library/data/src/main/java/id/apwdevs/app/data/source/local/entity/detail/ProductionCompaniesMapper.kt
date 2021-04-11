package id.apwdevs.app.data.source.local.entity.detail

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "production_companies_mapper",
    primaryKeys = ["id", "production_id"]//,
//    foreignKeys = [
//
//        // for movie detail entity
//        ForeignKey(
//            entity = FavDetailMovieEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["id"],
//            onDelete = ForeignKey.CASCADE,
//            onUpdate = ForeignKey.CASCADE
//        ),
//
//        // for tvshow detail entity
////        ForeignKey(
////                entity = FavDetailTvShowEntity::class,
////                parentColumns = ["id"],
////                childColumns = ["id"],
////                onDelete = ForeignKey.CASCADE,
////                onUpdate = ForeignKey.CASCADE
////        ),
//    ]
)
data class ProductionCompaniesMapper(
    @ColumnInfo(name = "id") val idMovieShow: Int,
    @ColumnInfo(name = "production_id") val productionId: Int
)
