package id.apwdevs.app.data.source.local.entity.detail

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "production_companies",
)
data class ProductionCompanies (

    @PrimaryKey
    @ColumnInfo(name = "production_id")
    val productionId: Int,

    @ColumnInfo(name = "logo_path")
    val logoPath: String?,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "origin_country")
    val originCountry: String
)