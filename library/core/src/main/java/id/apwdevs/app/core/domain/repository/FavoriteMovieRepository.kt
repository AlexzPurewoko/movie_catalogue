package id.apwdevs.app.core.domain.repository

import androidx.paging.PagingData
import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.Movies
import kotlinx.coroutines.flow.Flow

@Deprecated("will be deleted!")
interface FavoriteMovieRepository {
    fun getAllFavorites(): Flow<PagingData<Movies>>
    fun getFavoriteMovie(id: Int): Flow<DetailMovie>
    suspend fun checkIsFavorite(id: Int): Boolean
    suspend fun save(detailMovie: DetailMovie)
    suspend fun unFavorite(id: Int)
}

