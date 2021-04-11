package id.apwdevs.app.data.source.local.entity.detail.tvshow

import androidx.room.*
import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.local.entity.detail.GenreMapper
import id.apwdevs.app.data.source.local.entity.detail.ProductionCompanies
import id.apwdevs.app.data.source.local.entity.detail.ProductionCompaniesMapper

@Entity(tableName = "fav_detail_tvshow")
data class FavDetailTvShowEntity(

        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: Int,

        @ColumnInfo(name = "original_language")
        val originalLanguage: String,

        @ColumnInfo(name = "number_of_episodes")
        val numberOfEpisodes: Int,

        @ColumnInfo(name = "type")
        val type: String,

        @ColumnInfo(name = "backdrop_path")
        val backdropPath: String?,

        @ColumnInfo(name = "popularity")
        val popularity: Double,

        @ColumnInfo(name = "number_of_seasons")
        val numberOfSeasons: Int,

        @ColumnInfo(name = "vote_count")
        val voteCount: Int,

        @ColumnInfo(name = "first_air_date")
        val firstAirDate: String,

        @ColumnInfo(name = "overview")
        val overview: String,

        @ColumnInfo(name = "poster_path")
        val posterPath: String?,

        @ColumnInfo(name = "original_name")
        val originalName: String,

        @ColumnInfo(name = "vote_average")
        val voteAverage: Double,

        @ColumnInfo(name = "name")
        val name: String,

        @ColumnInfo(name = "tagline")
        val tagline: String,

        @ColumnInfo(name = "in_production")
        val inProduction: Boolean,

        @ColumnInfo(name = "last_air_date")
        val lastAirDate: String,

        @ColumnInfo(name = "homepage")
        val homepage: String?,

        @ColumnInfo(name = "status")
        val status: String
)

data class FavDetailTvShow(
        @Embedded val favDetailTvShow: FavDetailTvShowEntity,
        @Relation(
                parentColumn = "id",
                entityColumn = "genre_id",
                associateBy = Junction(GenreMapper::class)
        )
        val genres: List<Genres>,
        @Relation(
                parentColumn = "id",
                entityColumn = "production_id",
                associateBy = Junction(ProductionCompaniesMapper::class)
        )
        val productionCompanies: List<ProductionCompanies>,
        @Relation(
                parentColumn = "id",
                entityColumn = "owner_id"
        )
        val creators: List<CreatedByEntity>,
        @Relation(
                parentColumn = "id",
                entityColumn = "owner_id"
        )
        val lastAndNextEpisodeToAir: List<EpisodesToAir>,
        @Relation(
                parentColumn = "id",
                entityColumn = "owner_id"
        )
        val seasons: List<TvShowSeason>
)