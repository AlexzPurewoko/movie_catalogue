package id.apwdevs.app.core.domain.repository

import id.apwdevs.app.core.utils.State
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository<ItemDomainModel, DetailItemDomainModel> {

    fun getAllFavorites(): Flow<State<ItemDomainModel>>

    fun getFavoriteDetailItem(id: Int): Flow<State<DetailItemDomainModel>>

    suspend fun checkIsFavorite(id: Int): Boolean

    suspend fun save(detailTvShow: DetailItemDomainModel)

    suspend fun unFavorite(id: Int)
}