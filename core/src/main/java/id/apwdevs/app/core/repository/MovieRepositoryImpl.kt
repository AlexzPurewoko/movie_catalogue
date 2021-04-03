package id.apwdevs.app.core.repository

import androidx.paging.*
import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.Genre
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.repository.MoviesRepository
import id.apwdevs.app.core.utils.RemoteToDomainMapper
import id.apwdevs.app.data.mediator.MovieRemoteMediator
import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.local.room.AppDatabase
import id.apwdevs.app.data.source.remote.service.ApiService
import id.apwdevs.app.data.utils.Config
import id.apwdevs.app.data.utils.QueryType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class MovieRepositoryImpl constructor(
        private val service: ApiService,
        private val accessDb: AppDatabase
) : MoviesRepository {
    private val genres = mutableListOf<Genres>()
    override fun discoverPopularMovies(): Flow<PagingData<Movies>> {
        return getPaging(QueryType.POPULAR())
    }

    override fun searchMovies(query: String, includeAdult: Boolean): Flow<PagingData<Movies>> {
        return getPaging(QueryType.SEARCH(query, includeAdult))
    }

    override fun getDetailMovie(movieId: Int): Flow<DetailMovie> {
        return flow {
            val detailMovie = service.getDetailMovies(movieId.toString(), Config.TOKEN, "en-US")
            val transform = RemoteToDomainMapper.detailMovie(detailMovie)
            emit(transform)
        }
    }

    private fun getPaging(queryType: QueryType): Flow<PagingData<Movies>> {
        val pagingSourceFactory = { accessDb.movieDao().getAllMovies() }
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = MovieRemoteMediator(
                service, accessDb, queryType
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
            .map { pagingData ->
                getGenre()
                pagingData.map {
                    val allGenres = it.genreIds.data.map {
                        val item = genres.find { i -> i.id == it }
                        if(item == null) Genre(0, "")
                        else Genre(item.id, item.genreName)
                    }
                    Movies(
                        movieId = it.id, title = it.title, overview = it.overview,
                        language = it.language, genres = allGenres, posterPath = it.posterPath,
                        backdropPath = it.backdropPath, releaseDate = it.releaseDate, voteAverage = it.voteAverage,
                        voteCount = it.voteCount, adult = it.adult
                    )
                }
            }
    }

    private suspend fun getGenre() {
        if(genres.isNotEmpty()) return
        val genresFromDao = accessDb.genreDao().getAllGenres()
        genres.addAll(genresFromDao)
    }
    companion object {
        const val PAGE_SIZE = 20
    }
}