package id.apwdevs.app.core.domain.usecase

import androidx.paging.PagingData
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.model.TvShow
import kotlinx.coroutines.flow.Flow

interface DiscoverPopularUseCase {
    fun discoverPopularMovies(): Flow<PagingData<Movies>>
    fun discoverPopularTvShow(): Flow<PagingData<TvShow>>
}