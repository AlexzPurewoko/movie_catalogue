package id.apwdevs.app.core.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.repository.MovieRepository
import id.apwdevs.app.core.utils.Config
import id.apwdevs.app.core.utils.State
import id.apwdevs.app.core.utils.mapToDomain
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
            config = Config.pageConfig(),
            remoteMediator = PopularMovieRemoteMediator(moviesNetwork, caseDb),
            pagingSourceFactory = { caseDb.getAllDataPaging() }
        ).flow.map { pagingData ->
            getGenre()
            pagingData.map {
                it.mapToDomain(genres)
            }
        }
    }

    override fun searchMovies(query: String, includeAdult: Boolean): Flow<PagingData<Movies>> {
        return Pager(
            config = Config.pageConfig(),
            pagingSourceFactory = { SearchMoviePagingSource(moviesNetwork, query, includeAdult) }
        ).flow.map { pagingData ->
            getGenre()
            pagingData.map {
                it.mapToDomain(genres)
            }
        }
    }

    override fun getDetailMovie(movieId: Int): Flow<State<DetailMovie>> {
        return flow {
            emit(State.Loading())
            try {
                val detailMovie = moviesNetwork.getDetailMovies(movieId.toString())
                val transform = detailMovie.mapToDomain()
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
}