package id.apwdevs.app.core.domain.repository

import androidx.paging.PagingData
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.TvShow
import kotlinx.coroutines.flow.Flow

interface IFavoriteTvShowRepository {
    fun getAllFavorites(): Flow<PagingData<TvShow>>
    fun getFavoriteTvShow(id: Int): Flow<DetailTvShow>
    suspend fun checkIsFavorite(id: Int) : Boolean
    suspend fun save(detailTvShow: DetailTvShow)
    suspend fun unFavorite(id: Int)
}