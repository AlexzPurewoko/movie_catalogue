package id.apwdevs.app.core.repository

import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.Genre
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.repository.FavMovieRepository
import id.apwdevs.app.core.utils.DomainToEntityMapper
import id.apwdevs.app.core.utils.EntityToDomainMapper
import id.apwdevs.app.core.utils.State
import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.local.entity.detail.movie.FavDetailMovieEntity
import id.apwdevs.app.data.source.local.database.favlocal.FavoriteMovieSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteMovieRepoImpl(
    private val favoriteDataSource: FavoriteMovieSource
) : FavMovieRepository {
    override fun getAllFavorites(): Flow<State<List<Movies>>> {
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

    override fun getFavoriteDetailItem(id: Int): Flow<State<DetailMovie>> {
        return flow {
            emit(State.Loading())
            try {
                val data = favoriteDataSource.getFavorite(1)
                val mapper = EntityToDomainMapper.detailMovie(data)
                emit(State.Success(mapper))
            } catch (e: Exception) {
                emit(State.Error(e))
            }
        }
    }

    override suspend fun checkIsFavorite(id: Int): Boolean {
        return favoriteDataSource.isFavorite(id)
    }

    override suspend fun save(data: DetailMovie) {
        val mapped = DomainToEntityMapper.favDetailMovie(data)
        favoriteDataSource.save(mapped)
    }

    override suspend fun unFavorite(id: Int) {
        favoriteDataSource.deleteData(id)
    }

    private suspend fun mapData(entities: List<FavDetailMovieEntity>, listGenres: List<Genres>): List<Movies> {
        return entities.map { entity ->
            val idMappers = favoriteDataSource.genreMapper(entity.id)
            val genres = idMappers.map {
                val item = listGenres.find { i -> i.id == it }
                if (item == null) Genre(0, "")
                else Genre(item.id, item.genreName)
            }

            Movies(
                    movieId = entity.id, title = entity.title, overview = entity.overview,
                    language = entity.originalLanguage, genres = genres, posterPath = entity.posterPath,
                    backdropPath = entity.backdropPath, releaseDate = entity.releaseDate, voteAverage = entity.voteAverage,
                    voteCount = entity.voteCount, adult = entity.adult
            )
        }

    }


}

