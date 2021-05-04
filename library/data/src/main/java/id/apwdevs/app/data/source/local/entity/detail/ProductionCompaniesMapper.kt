package id.apwdevs.app.data.source.local.entity.detail

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "production_companies_mapper",
    primaryKeys = ["id", "production_id"]
)
data class ProductionCompaniesMapper(
    @ColumnInfo(name = "id") val idMovieShow: Int,
    @ColumnInfo(name = "production_id") val productionId: Int
)
