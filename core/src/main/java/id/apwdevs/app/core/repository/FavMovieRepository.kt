package id.apwdevs.app.core.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.local.paging.MoviePagingSource
import id.apwdevs.app.data.source.local.room.AppDatabase
import id.apwdevs.app.core.utils.DomainToEntityMapper
import id.apwdevs.app.core.utils.EntityToDomainMapper
import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.Genre
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.repository.IFavoriteMovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FavMovieRepository  constructor(
    private val accessDb: AppDatabase
): IFavoriteMovieRepository {
    private val genres = mutableListOf<Genres>()
    override fun getAllFavorites(): Flow<PagingData<Movies>> {
        return Pager(
            config = PagingConfig(MoviePagingSource.LOAD_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {MoviePagingSource(accessDb)}
        ).flow.map { pagingData ->
            getGenre()
            pagingData.map {
                val listGenres = accessDb.genreDao().genreIdsMapper(it.id)
                val genres = listGenres.map{
                    val item = genres.find { i -> i.id == it }
                    if(item == null) Genre(0, "")
                    else Genre(item.id, item.genreName)
                }
                Movies(
                        movieId = it.id, title = it.title, overview = it.overview,
                        language = it.originalLanguage, genres = genres, posterPath = it.posterPath,
                        backdropPath = it.backdropPath, releaseDate = it.releaseDate, voteAverage = it.voteAverage,
                        voteCount = it.voteCount, adult = it.adult
                )
            }
        }
    }

    override fun getFavoriteMovie(id: Int): Flow<DetailMovie> {
        return flow {
            val detailEntity = accessDb.favMovieDetail().getMovie(id)
            val detail = EntityToDomainMapper.detailMovie(detailEntity)
            emit(detail)
        }
    }

    override suspend fun checkIsFavorite(id: Int) : Boolean {
        return accessDb.favMovieDetail().isMovieExists(id)
    }

    override suspend fun save(detailMovie: DetailMovie) {
        val favEntity = DomainToEntityMapper.favDetailMovie(detailMovie)
        accessDb.favMovieDetail().insertFavDetailMovie(favEntity)
    }

    override suspend fun unFavorite(id: Int) {
        accessDb.favMovieDetail().deleteById(id)
    }

    private suspend fun getGenre() {
        if(genres.isNotEmpty()) return
        val genresFromDao = accessDb.genreDao().getAllGenres()
        genres.addAll(genresFromDao)
    }

}

