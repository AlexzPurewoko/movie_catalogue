package id.apwdevs.app.core.domain.usecase

import androidx.paging.PagingData
import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.model.TvShow
import kotlinx.coroutines.flow.Flow

interface FavoriteUseCase {
    fun getAllFavoriteMovies() : Flow<PagingData<Movies>>
    fun getAllFavoriteTvShows() : Flow<PagingData<TvShow>>
    fun getFavoriteMovie(id: Int): Flow<DetailMovie>
    fun getFavoriteTvShow(id: Int): Flow<DetailTvShow>

    suspend fun checkIsInFavorite(id: Int, isMovie: Boolean) : Boolean
    suspend fun saveFavoriteMovie(detailMovie: DetailMovie)
    suspend fun saveFavoriteTvShow(detailTvShow: DetailTvShow)
    suspend fun unFavorite(id: Int, isMovie: Boolean)
}