package id.apwdevs.app.core.repository

import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.TvShow
import id.apwdevs.app.core.domain.repository.FavoriteRepository
import id.apwdevs.app.core.utils.State
import kotlinx.coroutines.flow.Flow

class FavoriteTvShowRepoImpl : FavoriteRepository<TvShow, DetailTvShow> {
    override fun getAllFavorites(): Flow<State<TvShow>> {
        TODO("Not yet implemented")
    }

    override fun getFavoriteDetailItem(id: Int): Flow<State<DetailTvShow>> {
        TODO("Not yet implemented")
    }

    override suspend fun checkIsFavorite(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun save(detailTvShow: DetailTvShow) {
        TODO("Not yet implemented")
    }

    override suspend fun unFavorite(id: Int) {
        TODO("Not yet implemented")
    }

}