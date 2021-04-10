package id.apwdevs.app.core.repository

import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.repository.FavoriteRepository
import id.apwdevs.app.core.utils.State
import kotlinx.coroutines.flow.Flow

class FavoriteMovieRepoImpl : FavoriteRepository<Movies, DetailMovie> {
    override fun getAllFavorites(): Flow<State<Movies>> {
        TODO("Not yet implemented")
    }

    override fun getFavoriteDetailItem(id: Int): Flow<State<DetailMovie>> {
        TODO("Not yet implemented")
    }

    override suspend fun checkIsFavorite(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun save(detailTvShow: DetailMovie) {
        TODO("Not yet implemented")
    }

    override suspend fun unFavorite(id: Int) {
        TODO("Not yet implemented")
    }
}

