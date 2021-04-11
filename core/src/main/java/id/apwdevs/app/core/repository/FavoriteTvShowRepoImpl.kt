package id.apwdevs.app.core.repository

import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.Genre
import id.apwdevs.app.core.domain.model.TvShow
import id.apwdevs.app.core.domain.repository.FavoriteRepository
import id.apwdevs.app.core.utils.DomainToEntityMapper
import id.apwdevs.app.core.utils.EntityToDomainMapper
import id.apwdevs.app.core.utils.State
import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.local.entity.detail.tvshow.FavDetailTvShow
import id.apwdevs.app.data.source.local.entity.detail.tvshow.FavDetailTvShowEntity
import id.apwdevs.app.data.source.local.room.dbcase.FavoriteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTvShowRepoImpl(
        private val favoriteDataSource: FavoriteDataSource<FavDetailTvShowEntity, FavDetailTvShow>
) : FavoriteRepository<TvShow, DetailTvShow> {
    override fun getAllFavorites(): Flow<State<List<TvShow>>> {
        return flow {
            emit(State.Loading())
            try {
                val data = favoriteDataSource.getAllFavorite()
                val genres = favoriteDataSource.genres()
                val mapped = mapData(data, genres)

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
                val data = favoriteDataSource.getFavorite(1)
                val mapper = EntityToDomainMapper.detailTvShow(data)
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
        val mapped = DomainToEntityMapper.favDetailTvShow(data)
        favoriteDataSource.save(mapped)
    }

    override suspend fun unFavorite(id: Int) {
        favoriteDataSource.deleteData(id)
    }

    private suspend fun mapData(entities: List<FavDetailTvShowEntity>, listGenres: List<Genres>): List<TvShow> {
        return entities.map { entity ->
            val idMappers = favoriteDataSource.genreMapper(entity.id)
            val genres = idMappers.map {
                val item = listGenres.find { i -> i.id == it }
                if (item == null) Genre(0, "")
                else Genre(item.id, item.genreName)
            }

            TvShow(
                    tvId = entity.id, name = entity.name, firstAirDate = entity.firstAirDate, overview = entity.overview,
                    language = entity.originalLanguage, genres = genres, posterPath = entity.posterPath,
                    backdropPath = entity.backdropPath, voteAverage = entity.voteAverage, voteCount = entity.voteCount
            )
        }

    }
}