package id.apwdevs.app.data.source.local.entity.detail.tvshow

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
        tableName = "seasons_tvshow",
        foreignKeys = [
            ForeignKey(
                    entity = FavDetailTvShowEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["owner_id"],
                    onUpdate = ForeignKey.CASCADE,
                    onDelete = ForeignKey.CASCADE
            )
        ]
)
data class TvShowSeason(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "owner_id")
    val ownerId: Int,

    @ColumnInfo(name = "air_date")
    val airDate: String,

    @ColumnInfo(name = "overview")
    val overview: String,

    @ColumnInfo(name = "episode_count")
    val episodeCount: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "season_number")
    val seasonNumber: Int,

    @ColumnInfo(name = "poster_path")
    val posterPath: String?
)
