package id.apwdevs.app.data.source.local.entity.detail.tvshow

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "created_by",
    foreignKeys = [
        ForeignKey(
            entity = FavDetailTvShowEntity::class,
            parentColumns = ["id"],
            childColumns = ["owner_id"],
            onUpdate = CASCADE,
            onDelete = CASCADE
        )
    ]
)
data class CreatedByEntity (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "owner_id")
    val ownerId: Int,

    @ColumnInfo(name = "gender")
    val gender: Int,

    @ColumnInfo(name = "credit_id")
    val creditId: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "profile_path")
    val profilePath: String?,

){



}