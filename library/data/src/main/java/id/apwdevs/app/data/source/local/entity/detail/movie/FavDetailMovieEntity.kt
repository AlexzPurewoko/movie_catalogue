package id.apwdevs.app.data.source.local.entity.detail.movie

import androidx.room.*
import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.local.entity.detail.GenreMapper
import id.apwdevs.app.data.source.local.entity.detail.ProductionCompanies
import id.apwdevs.app.data.source.local.entity.detail.ProductionCompaniesMapper

@Entity(tableName = "fav_detail_movie")
data class FavDetailMovieEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "original_language")
    val originalLanguage: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String?,

    @ColumnInfo(name = "revenue")
    val revenue: Int,

    @ColumnInfo(name = "popularity")
    val popularity: Double,

    @ColumnInfo(name = "vote_count")
    val voteCount: Int,

    @ColumnInfo(name = "budget")
    val budget: Int,

    @ColumnInfo(name = "overview")
    val overview: String?,

    @ColumnInfo(name = "original_title")
    val originalTitle: String,

    @ColumnInfo(name = "runtime")
    val runtime: Int?,

    @ColumnInfo(name = "poster_path")
    val posterPath: String?,

    @ColumnInfo(name = "release_date")
    val releaseDate: String,

    @ColumnInfo(name = "vote_average")
    val voteAverage: Double,

    @ColumnInfo(name = "tagline")
    val tagline: String?,

    @ColumnInfo(name = "is_adult")
    val adult: Boolean,

    @ColumnInfo(name = "homepage_url")
    val homepage: String?,

    @ColumnInfo(name = "status")
    val status: String
)


data class FavDetailMovie(
    @Embedded val detailMovie: FavDetailMovieEntity,
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
    val productionCompanies: List<ProductionCompanies>
)