package id.apwdevs.app.core.domain.repository

import androidx.paging.PagingData
import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.utils.State
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun discoverPopularMovies(): Flow<PagingData<Movies>>
    fun searchMovies(query: String, includeAdult: Boolean): Flow<PagingData<Movies>>
    fun getDetailMovie(movieId: Int): Flow<DetailMovie>
}

interface MovieRepository {
    fun discoverPopularMovies(): Flow<PagingData<Movies>>
    fun searchMovies(query: String, includeAdult: Boolean): Flow<PagingData<Movies>>
    fun getDetailMovie(movieId: Int): Flow<State<DetailMovie>>
}

