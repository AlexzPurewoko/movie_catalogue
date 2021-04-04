package id.apwdevs.app.core.repository

import androidx.paging.*
import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.Genre
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.repository.MovieRepository
import id.apwdevs.app.core.utils.RemoteToDomainMapper
import id.apwdevs.app.core.utils.State
import id.apwdevs.app.data.mediator.PopularMovieRemoteMediator
import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.local.entity.RemoteKeysMovie
import id.apwdevs.app.data.source.local.entity.items.MovieEntity
import id.apwdevs.app.data.source.local.room.dbcase.PagingCaseDb
import id.apwdevs.app.data.source.remote.paging.SearchMoviePagingSource
import id.apwdevs.app.data.source.remote.service.ApiService
import id.apwdevs.app.data.utils.Config
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class MovieRepoImpl constructor(
        private val service: ApiService,
        private val caseDb: PagingCaseDb<MovieEntity, RemoteKeysMovie>
) : MovieRepository {
    private val genres = mutableListOf<Genres>()
    override fun discoverPopularMovies(): Flow<PagingData<Movies>> {
        return Pager(
                config = PagingConfig(
                        pageSize = 20,
                        prefetchDistance = 3,
                        initialLoadSize = 20,
                        enablePlaceholders = false
                ),
                remoteMediator = PopularMovieRemoteMediator(service, caseDb),
                pagingSourceFactory = { caseDb.getAllDataPaging() }
        ).flow.map { pagingData ->
            getGenre()
            pagingData.map {
                val allGenres = it.genreIds.data.map {
                    val item = genres.find { i -> i.id == it }
                    if (item == null) Genre(0, "")
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

    override fun searchMovies(query: String, includeAdult: Boolean): Flow<PagingData<Movies>> {
        return Pager(
                config = PagingConfig(
                        pageSize = 20,
                        prefetchDistance = 3,
                        initialLoadSize = 20,
                        enablePlaceholders = false
                ),
                pagingSourceFactory = { SearchMoviePagingSource(service, query, includeAdult) }
        ).flow.map { pagingData ->
            getGenre()
            pagingData.map {
                val allGenres = it.genreIds.map {
                    val item = genres.find { i -> i.id == it }
                    if (item == null) Genre(0, "")
                    else Genre(item.id, item.genreName)
                }
                Movies(
                        movieId = it.id, title = it.title, overview = it.overview,
                        language = it.originalLanguage, genres = allGenres, posterPath = it.posterPath,
                        backdropPath = it.backdropPath, releaseDate = it.releaseDate, voteAverage = it.voteAverage,
                        voteCount = it.voteCount, adult = it.adult
                )
            }
        }
    }

    override fun getDetailMovie(movieId: Int): Flow<State<DetailMovie>> {
        return flow {
            emit(State.Loading() as State<DetailMovie>)
            try {
                val detailMovie = service.getDetailMovies(movieId.toString(), Config.TOKEN, "en-US")
                val transform = RemoteToDomainMapper.detailMovie(detailMovie)
                emit(State.Success(transform))
            } catch (e: Throwable) {
                emit(State.Error(e) as State<DetailMovie>)
            }
        }
    }

    private suspend fun getGenre() {
        if (genres.isNotEmpty()) return
        val genresFromDao = caseDb.getGenres()
        genres.addAll(genresFromDao)
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}