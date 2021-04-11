package id.apwdevs.app.data.source.local.room.dao

import androidx.paging.PagingSource
import androidx.room.*
import id.apwdevs.app.data.source.local.entity.items.TvEntity

@Dao
interface TvShowDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvShow(movie: List<TvEntity>)

    @Delete(entity = TvEntity::class)
    suspend fun deleteTvShowsById(vararg deleteMapper: DeleteDaoMapper)

    @Query("DELETE FROM tvshows")
    suspend fun clearTvShows()

    @Query("SELECT * FROM tvshows ORDER BY page_at ASC")
    fun getAllTvShows(): PagingSource<Int, TvEntity>

    @Query("SELECT * FROM tvshows")
    suspend fun getAllTvShow(): List<TvEntity>
}