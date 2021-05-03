package id.apwdevs.app.data.source.local.room.dao

import androidx.room.*
import id.apwdevs.app.data.source.local.entity.detail.GenreMapper
import id.apwdevs.app.data.source.local.entity.detail.ProductionCompanies
import id.apwdevs.app.data.source.local.entity.detail.ProductionCompaniesMapper
import id.apwdevs.app.data.source.local.entity.detail.tvshow.*

@Dao
abstract class FavTvShowDao{

    @Transaction
    open suspend fun insertFavDetailTvShow(favDetail: FavDetailTvShow) {

        insertFavTvShow(favDetail.favDetailTvShow)

        val genreMapper = favDetail.genres.map {
            GenreMapper(favDetail.favDetailTvShow.id, it.id)
        }
        val prodCompaniesMapper = favDetail.productionCompanies.map {
            ProductionCompaniesMapper(favDetail.favDetailTvShow.id, it.productionId)
        }

        insertAllProdCompanies(favDetail.productionCompanies)
        insertCreatorsEntity(favDetail.creators)
        insertAiringEpisodes(favDetail.lastAndNextEpisodeToAir)
        insertAllSeason(favDetail.seasons)

        insertAllGenreMapper(genreMapper)
        insertAllProdCompaniesMapper(prodCompaniesMapper)
    }

    // general methods to create all entities

    @Insert(onConflict = OnConflictStrategy.ABORT)
    protected abstract suspend fun insertFavTvShow(favDetailTvShowEntity: FavDetailTvShowEntity)

    @Insert
    protected abstract suspend fun insertAllGenreMapper(genreMapper: List<GenreMapper>)

    @Insert
    protected abstract suspend fun insertAllProdCompaniesMapper(productionCompaniesMapper : List<ProductionCompaniesMapper>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertAllProdCompanies(productionCompanies : List<ProductionCompanies>)

    @Insert
    protected abstract suspend fun insertCreatorsEntity(createdByEntity: List<CreatedByEntity>)

    @Insert
    protected abstract suspend fun insertAiringEpisodes(episodesToAir: List<EpisodesToAir>)

    @Insert
    protected abstract suspend fun insertAllSeason(tvSeasons: List<TvShowSeason>)


    // delete
    @Query("DELETE FROM fav_detail_tvshow WHERE id=:tvId")
    abstract suspend fun deleteById(tvId: Int)

    // check if any elements with id
    @Query("SELECT EXISTS(SELECT * FROM fav_detail_tvshow WHERE id=:tvId)")
    abstract suspend fun isTvShowExists(tvId: Int): Boolean

    @Transaction
    @Query("SELECT * FROM fav_detail_tvshow WHERE id=:tvId")
    abstract suspend fun getTvShow(tvId: Int): FavDetailTvShow

    /**
     * For paging source
     */

    @Query("SELECT COUNT() FROM fav_detail_tvshow")
    abstract suspend fun getCount(): Long

    @Query("SELECT * FROM fav_detail_tvshow")
    abstract suspend fun getTvShowEntities(): List<FavDetailTvShowEntity>
}