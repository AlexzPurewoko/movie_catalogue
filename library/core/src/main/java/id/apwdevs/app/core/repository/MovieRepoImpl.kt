package id.apwdevs.app.core.repository

import androidx.paging.*
import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.Genre
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.repository.MovieRepository
import id.apwdevs.app.core.utils.RemoteToDomainMapper
import id.apwdevs.app.core.utils.State
import id.apwdevs.app.data.mediator.PopularMovieRemoteMediator
import id.apwdevs.app.data.source.local.database.paging.PagingCaseMovieDb
import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.remote.network.MoviesNetwork
import id.apwdevs.app.data.source.remote.paging.SearchMoviePagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class MovieRepoImpl constructor(
    private val moviesNetwork: MoviesNetwork,
    private val caseDb: PagingCaseMovieDb
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
                remoteMediator = PopularMovieRemoteMediator(moviesNetwork, caseDb),
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
                pagingSourceFactory = { SearchMoviePagingSource(moviesNetwork, query, includeAdult) }
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
            emit(State.Loading())
            try {
                val detailMovie = moviesNetwork.getDetailMovies(movieId.toString())
                val transform = RemoteToDomainMapper.detailMovie(detailMovie)
                emit(State.Success(transform))
            } catch (e: Throwable) {
                emit(State.Error(e))
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