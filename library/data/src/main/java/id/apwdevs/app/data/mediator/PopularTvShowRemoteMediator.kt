package id.apwdevs.app.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.sqlite.db.SimpleSQLiteQuery
import id.apwdevs.app.data.source.local.database.paging.PagingCaseTvShowDb
import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.local.entity.RemoteKeysTvShow
import id.apwdevs.app.data.source.local.entity.items.TvEntity
import id.apwdevs.app.data.source.remote.network.TvShowsNetwork
import id.apwdevs.app.data.utils.mapToEntity
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PopularTvShowRemoteMediator(
    private val tvShowsNetwork: TvShowsNetwork,
    private val caseDb: PagingCaseTvShowDb
) : RemoteMediator<Int, TvEntity>() {

    private var hasBeenUpdateGenre: Boolean = false

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TvEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeysClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)

                remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }
        try {
            retrieveGenres()
            val apiResponse = tvShowsNetwork.getPopularTvShows(page)
            val items = apiResponse.results
            val endOfPaginationReached = page + 1 > apiResponse.totalPages
            caseDb.provideTransaction {
                if (loadType == LoadType.REFRESH) {
                    caseDb.clearRemoteKeys()
                }
//                else if (loadType == LoadType.APPEND && page - 2 > 0) {
//                    deletePrevKeys(page)
//                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = items.map {
                    RemoteKeysTvShow(tvId = it.id.toLong(), prevKey = prevKey, nextKey = nextKey)
                }

                caseDb.insertAllRemoteKey(keys)

                val mappedMoviesToEntity = items.mapToEntity(page).toList()
                caseDb.insert(mappedMoviesToEntity)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    @Suppress("unused")
    private suspend fun deletePrevKeys(currentPage: Int) {
        val pageToBeDeleted = currentPage - 2
        val postQuery =
            if (pageToBeDeleted == 1) " IS NULL"
            else "=$pageToBeDeleted"
        caseDb.deleteKey(
            SimpleSQLiteQuery("DELETE FROM remote_keys_tvshow WHERE previous_key$postQuery")
        )
    }

    private suspend fun retrieveGenres() {
        if (!hasBeenUpdateGenre) {
            val retrieveFromApi = tvShowsNetwork.getTvShowGenre()
            val genres = retrieveFromApi.genres
            val mappedGenres = genres?.map {
                Genres(it.id, it.name)
            }
            mappedGenres?.let {
                caseDb.insertGenres(it)
                hasBeenUpdateGenre = true
            }
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, TvEntity>): RemoteKeysTvShow? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let {
            caseDb.remoteKeysId(it.id.toLong())
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, TvEntity>): RemoteKeysTvShow? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let {
            caseDb.remoteKeysId(it.id.toLong())
        }
    }

    private suspend fun getRemoteKeysClosestToCurrentPosition(state: PagingState<Int, TvEntity>): RemoteKeysTvShow? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let {
                caseDb.remoteKeysId(it.toLong())
            }
        }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}