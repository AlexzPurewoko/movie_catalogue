package id.apwdevs.app.core.repository

import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.TvShow
import id.apwdevs.app.core.domain.repository.FavTvShowRepository
import id.apwdevs.app.core.utils.State
import id.apwdevs.app.core.utils.mapToDomain
import id.apwdevs.app.core.utils.mapToEntity
import id.apwdevs.app.data.source.local.database.favlocal.FavoriteTvShowSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTvShowRepoImpl(
    private val favoriteDataSource: FavoriteTvShowSource
) : FavTvShowRepository {
    override fun getAllFavorites(): Flow<State<List<TvShow>>> {
        return flow {
            emit(State.Loading())
            try {
                val data = favoriteDataSource.getAllFavorite()
                val genres = favoriteDataSource.genres()
                val mapped =
                    data.mapToDomain(genres) { favoriteDataSource.genreMapper(it) }.toList()

                emit(State.Success(mapped))
            } catch (e: Exception) {
                emit(State.Error(e))
            }
        }
    }

    override fun getFavoriteDetailItem(id: Int): Flow<State<DetailTvShow>> {
        return flow {
            emit(State.Loading())
            try {
                val data = favoriteDataSource.getFavorite(id)
                val mapper = data.mapToDomain()
                emit(State.Success(mapper))
            } catch (e: Exception) {
                emit(State.Error(e))
            }
        }
    }

    override suspend fun checkIsFavorite(id: Int): Boolean {
        return favoriteDataSource.isFavorite(id)
    }

    override suspend fun save(data: DetailTvShow) {
        val mapped = data.mapToEntity()
        favoriteDataSource.save(mapped)
    }

    override suspend fun unFavorite(id: Int) {
        favoriteDataSource.deleteData(id)
    }
}