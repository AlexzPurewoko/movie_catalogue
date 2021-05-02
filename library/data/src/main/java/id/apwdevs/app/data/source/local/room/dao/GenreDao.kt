package id.apwdevs.app.data.source.local.room.dao

import androidx.room.*
import id.apwdevs.app.data.source.local.entity.Genres

@Dao
@Suppress("unused")
interface GenreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<Genres>)

    @Delete
    suspend fun deleteGenres(vararg genres: Genres)

    @Query("SELECT * FROM genres")
    suspend fun getAllGenres(): List<Genres>

    @Query("DELETE FROM genres WHERE genre_id=:id")
    suspend fun deleteGenreById(id: Int)

    @Query("SELECT genre_id FROM genre_mapper WHERE id=:ownerId")
    suspend fun genreIdsMapper(ownerId: Int): List<Int>
}