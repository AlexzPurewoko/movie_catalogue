package id.apwdevs.app.core.domain.usecase

import androidx.paging.PagingData
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.model.TvShow
import id.apwdevs.app.core.domain.repository.MoviesRepository
import id.apwdevs.app.core.domain.repository.TvShowRepository
import kotlinx.coroutines.flow.Flow

class SearchInteractor constructor(
        private val movieRepository: MoviesRepository,
        private val tvShowRepository: TvShowRepository
): SearchUseCase {
    override fun searchMovie(query: String, includeAdult: Boolean): Flow<PagingData<Movies>> {
        return movieRepository.searchMovies(query, includeAdult)
    }

    override fun searchTvShow(query: String, includeAdult: Boolean): Flow<PagingData<TvShow>> {
        return tvShowRepository.searchTvShow(query, includeAdult)
    }

}