package id.apwdevs.app.core.repository

import androidx.paging.*
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.Genre
import id.apwdevs.app.core.domain.model.TvShow
import id.apwdevs.app.core.domain.repository.TvShowRepository
import id.apwdevs.app.core.utils.RemoteToDomainMapper
import id.apwdevs.app.data.mediator.TvShowRemoteMediator
import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.local.room.AppDatabase
import id.apwdevs.app.data.source.remote.service.ApiService
import id.apwdevs.app.data.utils.Config
import id.apwdevs.app.data.utils.QueryType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class TvShowRepositoryImpl constructor(
        private val service: ApiService,
        private val accessDb: AppDatabase
) : TvShowRepository {

    private val genres = mutableListOf<Genres>()
    override fun discoverPopularTvShow(): Flow<PagingData<TvShow>> {
        return getPaging(QueryType.POPULAR())
    }

    override fun searchTvShow(query: String, includeAdult: Boolean): Flow<PagingData<TvShow>> {
        return getPaging(QueryType.SEARCH(query, includeAdult))
    }

    override fun getDetailTvShow(tvId: Int): Flow<DetailTvShow> {
        return flow {
            val detailTvShow = service.getDetailTvShows(tvId.toString(), Config.TOKEN, "en-US")
            val transform = RemoteToDomainMapper.detailTvShow(detailTvShow)
            emit(transform)
        }
    }

    private fun getPaging(queryType: QueryType): Flow<PagingData<TvShow>> {
        val pagingSourceFactory = { accessDb.tvShowDao().getAllTvShows() }
        return Pager(
            config = PagingConfig(PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = TvShowRemoteMediator(
                service, accessDb, queryType
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
            .map { pagingData ->
                getGenre()
                pagingData.map {
                    val allGenres = it.genreIds.data.map {
                        val item = genres.find { i -> i.id == it }
                        if(item == null) Genre(0, "")
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


    private suspend fun getGenre() {
        if(genres.isNotEmpty()) return
        val genresFromDao = accessDb.genreDao().getAllGenres()
        genres.addAll(genresFromDao)
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}