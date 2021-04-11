package id.apwdevs.app.core.domain.usecase

import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.model.TvShow
import id.apwdevs.app.core.utils.DataType
import id.apwdevs.app.core.utils.State
import kotlinx.coroutines.flow.Flow

interface FavUseCase {
    fun getAllFavoriteMovies(): Flow<State<List<Movies>>>
    fun getAllFavoriteTvShows(): Flow<State<List<TvShow>>>
    fun getFavoriteMovie(id: Int): Flow<State<DetailMovie>>
    fun getFavoriteTvShow(id: Int): Flow<State<DetailTvShow>>

    suspend fun checkIsInFavorite(id: Int, dataType: DataType): Boolean
    suspend fun saveFavoriteMovie(detailMovie: DetailMovie)
    suspend fun saveFavoriteTvShow(detailTvShow: DetailTvShow)
    suspend fun unFavorite(id: Int, dataType: DataType)
}