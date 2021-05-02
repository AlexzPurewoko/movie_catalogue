package id.apwdevs.app.core.repository

import androidx.paging.*
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.Genre
import id.apwdevs.app.core.domain.model.TvShow
import id.apwdevs.app.core.domain.repository.TvShowRepository
import id.apwdevs.app.core.utils.RemoteToDomainMapper
import id.apwdevs.app.core.utils.State
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
                config = PagingConfig(
                        pageSize = 20,
                        prefetchDistance = 3,
                        initialLoadSize = 20,
                        enablePlaceholders = false
                ),
                remoteMediator = PopularTvShowRemoteMediator(tvShowsNetwork, caseDb),
                pagingSourceFactory = { caseDb.getAllDataPaging() }
        ).flow.map { pagingData ->
            getGenre()
            pagingData.map {
                val allGenres = it.genreIds.data.map {
                    val item = genres.find { i -> i.id == it }
                    if (item == null) Genre(0, "")
                    else Genre(item.id, item.genreName)
                }
                TvShow(
                        tvId = it.id, name = it.name, firstAirDate = it.firstAirDate, overview = it.overview,
                        language = it.language, genres = allGenres, posterPath = it.posterPath,
                        backdropPath = it.backdropPath, voteAverage = it.voteAverage, voteCount = it.voteCount
                )
            }
        }
    }

    override fun searchTvShow(query: String, includeAdult: Boolean): Flow<PagingData<TvShow>> {
        return Pager(
                config = PagingConfig(
                        pageSize = 20,
                        prefetchDistance = 3,
                        initialLoadSize = 20,
                        enablePlaceholders = false
                ),
                pagingSourceFactory = { SearchTvShowPagingSource(tvShowsNetwork, query, includeAdult) }
        ).flow.map { pagingData ->
            getGenre()
            pagingData.map {
                val allGenres = it.genreIds.map {
                    val item = genres.find { i -> i.id == it }
                    if (item == null) Genre(0, "")
                    else Genre(item.id, item.genreName)
                }
                TvShow(
                        tvId = it.id, name = it.name, firstAirDate = it.firstAirDate, overview = it.overview,
                        language = it.originalLanguage, genres = allGenres, posterPath = it.posterPath,
                        backdropPath = it.backdropPath, voteAverage = it.voteAverage, voteCount = it.voteCount
                )
            }
        }
    }

    override fun getDetailTvShow(tvId: Int): Flow<State<DetailTvShow>> {
        return flow {
            emit(State.Loading())
            try {
                val detailMovie = tvShowsNetwork.getDetailTvShows(tvId.toString())
                val transform = RemoteToDomainMapper.detailTvShow(detailMovie)
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