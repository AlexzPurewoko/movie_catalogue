package id.apwdevs.app.core.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.TvShow
import id.apwdevs.app.core.domain.repository.TvShowRepository
import id.apwdevs.app.core.utils.Config
import id.apwdevs.app.core.utils.State
import id.apwdevs.app.core.utils.mapToDomain
import id.apwdevs.app.data.mediator.PopularTvShowRemoteMediator
import id.apwdevs.app.data.source.local.database.paging.PagingCaseTvShowDb
import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.remote.network.TvShowsNetwork
import id.apwdevs.app.data.source.remote.paging.SearchTvShowPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class TvShowRepoImpl constructor(
    private val tvShowsNetwork: TvShowsNetwork,
    private val caseDb: PagingCaseTvShowDb
) : TvShowRepository {

    private val genres = mutableListOf<Genres>()


    override fun discoverPopularTvShow(): Flow<PagingData<TvShow>> {
        return Pager(
                config = Config.pageConfig(),
                remoteMediator = PopularTvShowRemoteMediator(tvShowsNetwork, caseDb),
                pagingSourceFactory = { caseDb.getAllDataPaging() }
        ).flow.map { pagingData ->
            getGenre()
            pagingData.map {
                it.mapToDomain(genres)
            }
        }
    }

    override fun searchTvShow(query: String, includeAdult: Boolean): Flow<PagingData<TvShow>> {
        return Pager(
                config = Config.pageConfig(),
                pagingSourceFactory = { SearchTvShowPagingSource(tvShowsNetwork, query, includeAdult) }
        ).flow.map { pagingData ->
            getGenre()
            pagingData.map {
                it.mapToDomain(genres)
            }
        }
    }

    override fun getDetailTvShow(tvId: Int): Flow<State<DetailTvShow>> {
        return flow {
            emit(State.Loading())
            try {
                val detailMovie = tvShowsNetwork.getDetailTvShows(tvId.toString())
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