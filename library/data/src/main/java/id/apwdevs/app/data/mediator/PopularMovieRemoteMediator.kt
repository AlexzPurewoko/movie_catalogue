package id.apwdevs.app.data.mediator

import android.annotation.SuppressLint
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.sqlite.db.SimpleSQLiteQuery
import id.apwdevs.app.data.source.local.database.paging.PagingCaseMovieDb
import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.local.entity.RemoteKeysMovie
import id.apwdevs.app.data.source.local.entity.items.MovieEntity
import id.apwdevs.app.data.source.remote.network.MoviesNetwork
import id.apwdevs.app.data.utils.mapToEntity
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PopularMovieRemoteMediator(
    private val moviesNetwork: MoviesNetwork,
    private val caseDb: PagingCaseMovieDb
) : RemoteMediator<Int, MovieEntity>() {

    private var hasBeenUpdateGenre: Boolean = false

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    @SuppressLint("VisibleForTests")
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                Log.d("TRY", "remote mediator REFRESH")
                val remoteKeys = getRemoteKeysClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                Log.d("TRY", "remote mediator PREPEND")
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
            val apiResponse = moviesNetwork.getPopularMovies(page)
            val items = apiResponse.results
            val endOfPaginationReached = page + 1 > apiResponse.totalPages
            caseDb.provideTransaction {
                if (loadType == LoadType.REFRESH) {
                    caseDb.clearRemoteKeys()
                } else if (loadType == LoadType.APPEND && page - 2 > 0) {
                    deletePrevKeys(page)
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = items.map {
                    RemoteKeysMovie(movieId = it.id.toLong(), prevKey = prevKey, nextKey = nextKey)
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

    private suspend fun deletePrevKeys(currentPage: Int) {
        val pageToBeDeleted = currentPage - 2
        val postQuery =
            if (pageToBeDeleted == 1) " IS NULL"
            else "=$pageToBeDeleted"
        caseDb.deleteKey(
            SimpleSQLiteQuery("DELETE FROM remote_keys_movie WHERE previous_key$postQuery")
        )
    }

    private suspend fun retrieveGenres() {
        if (!hasBeenUpdateGenre) {
            val retrieveFromApi = moviesNetwork.getMovieGenre()
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

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieEntity>): RemoteKeysMovie? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let {
            caseDb.remoteKeysId(it.id.toLong())
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieEntity>): RemoteKeysMovie? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let {
            caseDb.remoteKeysId(it.id.toLong())
        }
    }

    private suspend fun getRemoteKeysClosestToCurrentPosition(state: PagingState<Int, MovieEntity>): RemoteKeysMovie? {
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


