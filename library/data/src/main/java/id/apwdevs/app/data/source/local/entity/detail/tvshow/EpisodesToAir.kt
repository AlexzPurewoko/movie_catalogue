package id.apwdevs.app.data.source.local.entity.detail.tvshow

import androidx.room.*

@Entity(
    tableName = "episodes_to_air",
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
@TypeConverters(EpisodesToAir.AiringTypeConverter::class)
data class EpisodesToAir(

    @PrimaryKey
    @ColumnInfo(name = "episode_id")
    val id: Int,

    @ColumnInfo(name = "owner_id")
    val ownerId: Int,

    @ColumnInfo(name = "production_code")
    val productionCode: String,

    @ColumnInfo(name = "air_date")
    val airDate: String,

    @ColumnInfo(name = "overview")
    val overview: String,

    @ColumnInfo(name = "episode_number")
    val episodeNumber: Int,

    @ColumnInfo(name = "vote_average")
    val voteAverage: Double,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "season_number")
    val seasonNumber: Int,

    @ColumnInfo(name = "still_path")
    val stillPath: String?,

    @ColumnInfo(name = "vote_count")
    val voteCount: Int,

    @ColumnInfo(name = "airing_type")
    val airingType: AiringType?
) {
    enum class AiringType(val typeId: Int) {
        LAST_AIR(0x3a),
        NEXT_AIR(0x4a)
    }

    @Suppress("unused")
    class AiringTypeConverter {
        @TypeConverter
        fun convertIntToAiringType(typeId: Int): AiringType? {
            return when (typeId) {
                0x3a -> AiringType.LAST_AIR
                0X4a -> AiringType.NEXT_AIR
                else -> null
            }
        }

        @TypeConverter
        fun convertAiringTypeToIntType(airingType: AiringType?): Int {
            return airingType?.typeId ?: 0
        }
    }
}
