package id.apwdevs.app.core.domain.usecase

import androidx.paging.PagingData
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.model.TvShow
import kotlinx.coroutines.flow.Flow

interface SearchUseCase {
    fun searchMovie(query: String, includeAdult: Boolean): Flow<PagingData<Movies>>
    fun searchTvShow(query: String, includeAdult: Boolean): Flow<PagingData<TvShow>>
}

