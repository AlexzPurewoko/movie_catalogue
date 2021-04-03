package id.apwdevs.app.data.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.local.entity.RemoteKeysTvShow
import id.apwdevs.app.data.source.local.entity.converters.GenreIdsTypeConverter
import id.apwdevs.app.data.source.local.entity.items.TvEntity
import id.apwdevs.app.data.source.local.room.AppDatabase
import id.apwdevs.app.data.source.remote.response.TvShowItemResponse
import id.apwdevs.app.data.source.remote.service.ApiService
import id.apwdevs.app.data.utils.Config
import id.apwdevs.app.data.utils.QueryType
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

@Deprecated(
        "This class will be deleted and changed to a new mediator",
        replaceWith = ReplaceWith("PopularTvShowRemoteMediator::class"),
)
@OptIn(ExperimentalPagingApi::class)
class TvShowRemoteMediator(
        private val service: ApiService,
        private val accessDb: AppDatabase,
        private val queryType: QueryType
): RemoteMediator<Int, TvEntity>() {

    private var hasBeenUpdateGenre: Boolean = false

    override suspend fun load(
            loadType: LoadType,
            state: PagingState<Int, TvEntity>
    ): MediatorResult {

        val page = when(loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeysClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                        ?: throw InvalidObjectException("Remote Key and the prev Key should not be null")

                remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                if(remoteKeys?.nextKey == null) throw InvalidObjectException("Remote key should not be null for $loadType")
                remoteKeys.nextKey
            }
        }
        try {

            retrieveGenres()
            val apiResponse = when(queryType) {
                is QueryType.SEARCH -> service.searchTvShow(Config.TOKEN, page, queryType.query, queryType.includeAdult)
                is QueryType.POPULAR -> service.getPopularTvShows(Config.TOKEN, "en-US", page)
            }
            val items = apiResponse.results
            val endOfPaginationReached = items.isEmpty()
            accessDb.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    accessDb.remoteKeysTvShowDao().clearRemoteKeys()
                    accessDb.tvShowDao().clearTvShows()
                }

                val prevKey = if(page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if(endOfPaginationReached) null else page + 1
                val keys = items.map {
                    RemoteKeysTvShow(tvId = it.id.toLong(), prevKey = prevKey, nextKey = nextKey)
                }
                accessDb.remoteKeysTvShowDao().insertAll(keys)

                val mappedMoviesToEntity = mapMoviesToEntity(items)
                accessDb.tvShowDao().insertTvShow(mappedMoviesToEntity)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException){
            return MediatorResult.Error(e)
        } catch (e: HttpException){
            return MediatorResult.Error(e)
        }
    }

    private fun mapMoviesToEntity(items: List<TvShowItemResponse>): List<TvEntity> {

        return items.map {
            val convertedGenres = GenreIdsTypeConverter.GenreIdData(
                    data = it.genreIds
            )
            Log.e("HHHHHHMOVIETV", "bc : ${it.backdropPath}")
            TvEntity(
                    id = it.id, name = it.name,
                    overview = it.overview, language = it.originalLanguage,
                    genreIds = convertedGenres, posterPath = it.posterPath, backdropPath = it.backdropPath
                    ?: "",
                    firstAirDate = it.firstAirDate, voteAverage = it.voteAverage, voteCount = it.voteCount, page = 1
            )
        }
    }

    private suspend fun retrieveGenres() {
        if(!hasBeenUpdateGenre) {
            val retrieveFromApi = service.getTvShowGenre(Config.TOKEN)
            val genres = retrieveFromApi.genres
            val mappedGenres = genres.map {
                Genres(it.id, it.name)
            }
            accessDb.genreDao().insertGenres(mappedGenres)
            hasBeenUpdateGenre = true
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, TvEntity>): RemoteKeysTvShow? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let {
            accessDb.remoteKeysTvShowDao().remoteKeysTvShowId(it.id.toLong())
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, TvEntity>): RemoteKeysTvShow? {
        return state.pages.firstOrNull {it.data.isNotEmpty()}?.data?.firstOrNull()?.let {
            accessDb.remoteKeysTvShowDao().remoteKeysTvShowId(it.id.toLong())
        }
    }

    private suspend fun getRemoteKeysClosestToCurrentPosition(state: PagingState<Int, TvEntity>): RemoteKeysTvShow? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let {
                accessDb.remoteKeysTvShowDao().remoteKeysTvShowId(it.toLong())
            }
        }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}