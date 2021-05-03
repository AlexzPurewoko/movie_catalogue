package id.apwdevs.app.data.source.local.room.dao

import androidx.room.*
import id.apwdevs.app.data.source.local.entity.detail.GenreMapper
import id.apwdevs.app.data.source.local.entity.detail.ProductionCompanies
import id.apwdevs.app.data.source.local.entity.detail.ProductionCompaniesMapper
import id.apwdevs.app.data.source.local.entity.detail.movie.FavDetailMovie
import id.apwdevs.app.data.source.local.entity.detail.movie.FavDetailMovieEntity

@Dao
abstract class FavDetailMovieDao {

    // create detail movie
    @Transaction
    open suspend fun insertFavDetailMovie(favDetailMovie: FavDetailMovie) {

        insertFavDetailMovieEntity(favDetailMovie.detailMovie)
        insertAllProdCompanies(favDetailMovie.productionCompanies)

        val genreMapper = favDetailMovie.genres.map {
            GenreMapper(favDetailMovie.detailMovie.id, it.id)
        }

        insertAllGenreMapper(genreMapper)

        val prodCompaniesMapper = favDetailMovie.productionCompanies.map {
            ProductionCompaniesMapper(favDetailMovie.detailMovie.id, it.productionId)
        }
        insertAllProdCompaniesMapper(prodCompaniesMapper)


    }

    // general methods to create all entities
    @Insert
    protected abstract suspend fun insertAllGenreMapper(genreMapper: List<GenreMapper>)

    @Insert
    protected abstract suspend fun insertAllProdCompaniesMapper(productionCompaniesMapper : List<ProductionCompaniesMapper>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertAllProdCompanies(productionCompanies : List<ProductionCompanies>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    protected abstract suspend fun insertFavDetailMovieEntity(favDetailMovieEntity : FavDetailMovieEntity)


    // delete
    @Transaction
    open suspend fun deleteById(movieId: Int) {
        deleteMovieById(movieId)
        deleteProductionMapperReferencedBy(movieId)
        deleteGenreMapperReferencedBy(movieId)
    }

    @Query("DELETE FROM fav_detail_movie WHERE id=:movieId")
    protected abstract suspend fun deleteMovieById(movieId: Int)

    @Query("DELETE FROM production_companies_mapper WHERE id=:movieId")
    protected abstract suspend fun deleteProductionMapperReferencedBy(movieId: Int)

    @Query("DELETE FROM genre_mapper WHERE id=:movieId")
    protected abstract suspend fun deleteGenreMapperReferencedBy(movieId: Int)


    // check if any elements with id
    @Query("SELECT EXISTS(SELECT * FROM fav_detail_movie WHERE id=:movieId)")
    abstract suspend fun isMovieExists(movieId: Int): Boolean

    @Transaction
    @Throws(Exception::class)
    @Query("SELECT * FROM fav_detail_movie WHERE id=:movieId")
    abstract suspend fun getMovie(movieId: Int): FavDetailMovie

    /**
     * For paging source only
     */
    @Query("SELECT COUNT() FROM fav_detail_movie")
    abstract suspend fun getCount(): Long

    @Query("SELECT * FROM fav_detail_movie")
    abstract suspend fun getMovieEntities(): List<FavDetailMovieEntity>

}