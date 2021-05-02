package id.apwdevs.app.data.source.local.room.dao

import androidx.paging.PagingSource
import androidx.room.*
import id.apwdevs.app.data.source.local.entity.items.MovieEntity

@Dao
@Suppress("unused")
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: List<MovieEntity>)

    @Delete(entity = MovieEntity::class)
    suspend fun deleteMoviesById(vararg deleteMapper: DeleteDaoMapper)

    @Query("DELETE FROM movies")
    suspend fun clearMovies()

    @Query("SELECT * FROM movies ORDER BY page_at ASC")
    fun getAllMovies(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movies")
    suspend fun getAllMovie(): List<MovieEntity>
}

