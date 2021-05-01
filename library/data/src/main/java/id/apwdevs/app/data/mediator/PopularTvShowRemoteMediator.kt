package id.apwdevs.app.data.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.sqlite.db.SimpleSQLiteQuery
import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.local.entity.RemoteKeysTvShow
import id.apwdevs.app.data.source.local.entity.converters.GenreIdsTypeConverter
import id.apwdevs.app.data.source.local.entity.items.TvEntity
import id.apwdevs.app.data.source.local.room.dbcase.paging.PagingCaseTvShowDb
import id.apwdevs.app.data.source.remote.response.TvShowItemResponse
import id.apwdevs.app.data.source.remote.service.ApiService
import id.apwdevs.app.data.utils.Config
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PopularTvShowRemoteMediator(
    private val service: ApiService,
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
                Log.d("TRY", "remote mediator REFRESH")
                val remoteKeys = getRemoteKeysClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                Log.d("TRY", "remote mediator PREPEND")
                val remoteKeys = getRemoteKeyForFirstItem(state)
//                    ?: throw InvalidObjectException("Remote Key and the prev Key should not be null")

                remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
//                if(remoteKeys?.nextKey == null) throw InvalidObjectException("Remote key should not be null for $loadType")
                Log.d("TRY", "remote mediator NEXT #${remoteKeys?.nextKey}")
                remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }
        try {
            Log.d("TRY", "PAGE #${page}")
            retrieveGenres()
            val apiResponse = service.getPopularTvShows(Config.TOKEN, "en-US", page)

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
                    RemoteKeysTvShow(tvId = it.id.toLong(), prevKey = prevKey, nextKey = nextKey)
                }
                Log.e("REMOTEKEY", "page $page. list -> ${keys.toString()}")
                caseDb.insertAllRemoteKey(keys)

                val saved = caseDb.getAllRemoteKey()
                Log.e("REMOTEKEY", "page $page in DAO---. list -> ${saved.toString()}")

                val mappedMoviesToEntity = mapMoviesToEntity(items, page)
                caseDb.insert(mappedMoviesToEntity)
                val saved2 = caseDb.getAllData()

                Log.e("REMOTEKEY", "page $page in ALL MOVIES---. list -> ${saved2.size}")
            }

            Log.d("TRY", "remote mediator after TRANSACTION ${apiResponse.totalPages}")
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {

            Log.e("TRYERROR", "remote mediator error", e)
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            Log.e("TRYERROR", "remote mediator error", e)
            return MediatorResult.Error(e)
        }
    }

    private suspend fun deletePrevKeys(currentPage: Int) {
        val pageToBeDeleted = currentPage - 2
        val postQuery =
            if (pageToBeDeleted == 1) " IS NULL"
            else "=$pageToBeDeleted"
        caseDb.deleteKey(
            SimpleSQLiteQuery("DELETE FROM remote_keys_tvshow WHERE previous_key$postQuery")
        )
    }

    private fun mapMoviesToEntity(items: List<TvShowItemResponse>, page: Int): List<TvEntity> {

        return items.map {
            val convertedGenres = GenreIdsTypeConverter.GenreIdData(
                data = it.genreIds
            )
            Log.e("HHHHHHMOVIETV", "bc : ${it.backdropPath}")
            TvEntity(
                id = it.id,
                name = it.name,
                overview = it.overview,
                language = it.originalLanguage,
                genreIds = convertedGenres,
                posterPath = it.posterPath,
                backdropPath = it.backdropPath
                    ?: "",
                firstAirDate = it.firstAirDate,
                voteAverage = it.voteAverage,
                voteCount = it.voteCount,
                page = page
            )
        }
    }

    private suspend fun retrieveGenres() {
        if (!hasBeenUpdateGenre) {
            val retrieveFromApi = service.getTvShowGenre(Config.TOKEN)
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
            val f = caseDb.remoteKeysId(it.id.toLong())

            Log.e("REMOTEKEY", "GET ${it.id}. RESULT ${f?.nextKey}")
            f

        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, TvEntity>): RemoteKeysTvShow? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let {
            val d = caseDb.remoteKeysId(it.id.toLong())
            d
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