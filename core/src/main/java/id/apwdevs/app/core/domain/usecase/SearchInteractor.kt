package id.apwdevs.app.core.domain.usecase

import androidx.paging.PagingData
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.model.TvShow
import id.apwdevs.app.core.domain.repository.IMoviesRepository
import id.apwdevs.app.core.domain.repository.ITvShowRepository
import kotlinx.coroutines.flow.Flow

class SearchInteractor constructor(
    private val movieRepository: IMoviesRepository,
    private val tvShowRepository: ITvShowRepository
): SearchUseCase {
    override fun searchMovie(query: String, includeAdult: Boolean): Flow<PagingData<Movies>> {
        return movieRepository.searchMovies(query, includeAdult)
    }

    override fun searchTvShow(query: String, includeAdult: Boolean): Flow<PagingData<TvShow>> {
        return tvShowRepository.searchTvShow(query, includeAdult)
    }

}