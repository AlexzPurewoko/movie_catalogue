package id.apwdevs.app.core.domain.usecase

import androidx.paging.PagingData
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.model.TvShow
import id.apwdevs.app.core.domain.repository.MoviesRepository
import id.apwdevs.app.core.domain.repository.TvShowRepository
import kotlinx.coroutines.flow.Flow

class DiscoverPopularInteractor constructor(
        private val movieRepository: MoviesRepository,
        private val tvshowRepository: TvShowRepository
) : DiscoverPopularUseCase {
    override fun discoverPopularMovies(): Flow<PagingData<Movies>> {
        return movieRepository.discoverPopularMovies()
    }

    override fun discoverPopularTvShow(): Flow<PagingData<TvShow>> {
        return tvshowRepository.discoverPopularTvShow()
    }
}