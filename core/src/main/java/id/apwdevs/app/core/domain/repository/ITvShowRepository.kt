package id.apwdevs.app.core.domain.repository

import androidx.paging.PagingData
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.model.TvShow
import kotlinx.coroutines.flow.Flow

interface ITvShowRepository {
    fun discoverPopularTvShow() : Flow<PagingData<TvShow>>
    fun searchTvShow(query: String, includeAdult: Boolean): Flow<PagingData<TvShow>>
    fun getDetailTvShow(tvId: Int): Flow<DetailTvShow>
}