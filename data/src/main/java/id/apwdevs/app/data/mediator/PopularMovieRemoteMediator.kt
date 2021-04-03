package id.apwdevs.app.data.mediator

import android.annotation.SuppressLint
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import androidx.sqlite.db.SimpleSQLiteQuery
import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.local.entity.RemoteKeysMovie
import id.apwdevs.app.data.source.local.entity.converters.GenreIdsTypeConverter
import id.apwdevs.app.data.source.local.entity.items.MovieEntity
import id.apwdevs.app.data.source.local.room.AppDatabase
import id.apwdevs.app.data.source.remote.response.MovieItemResponse
import id.apwdevs.app.data.source.remote.service.ApiService
import id.apwdevs.app.data.utils.Config
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PopularMovieRemoteMediator(
    private val service: ApiService,
    private val accessDb: AppDatabase
): RemoteMediator<Int, MovieEntity>() {

    private var hasBeenUpdateGenre: Boolean = false

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }
    @SuppressLint("VisibleForTests")
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        val page = when(loadType) {
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
            val apiResponse = service.getPopularMovies(Config.TOKEN, "en-US", page)
            val items = apiResponse.results
            val endOfPaginationReached = page+1 > apiResponse.totalPages
            accessDb.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    accessDb.remoteKeysMovieDao().clearRemoteKeys()
                } else if (loadType == LoadType.APPEND && page - 2 > 0) {
                    deletePrevKeys(page)
                }
                val prevKey = if(page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if(endOfPaginationReached) null else page + 1
                val keys = items.map {
                    RemoteKeysMovie(movieId = it.id.toLong(), prevKey = prevKey, nextKey = nextKey)
                }
                Log.e("REMOTEKEY", "page $page. list -> ${keys.toString()}")
                accessDb.remoteKeysMovieDao().insertAll(keys)

                val saved = accessDb.remoteKeysMovieDao().getAll()
                Log.e("REMOTEKEY", "page $page in DAO---. list -> ${saved.toString()}")

                val mappedMoviesToEntity = mapMoviesToEntity(items, page)
                accessDb.movieDao().insertMovie(mappedMoviesToEntity)
                val saved2 = accessDb.movieDao().getAllMovie()

                Log.e("REMOTEKEY", "page $page in ALL MOVIES---. list -> ${saved2.size}")
            }

            Log.d("TRY", "remote mediator after TRANSACTION ${apiResponse.totalPages}")
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException){

            Log.e("TRYERROR", "remote mediator error", e)
            return MediatorResult.Error(e)
        } catch (e: HttpException){
            Log.e("TRYERROR", "remote mediator error", e)
            return MediatorResult.Error(e)
        }
    }

    private suspend fun deletePrevKeys(currentPage: Int){
        val pageToBeDeleted = currentPage - 2
        val postQuery =
                if(pageToBeDeleted == 1) " IS NULL"
                else "=$pageToBeDeleted"
        accessDb.remoteKeysMovieDao().deleteKeys(
                SimpleSQLiteQuery("DELETE FROM remote_keys_movie WHERE previous_key$postQuery")
        )
    }

    private fun mapMoviesToEntity(items: List<MovieItemResponse>, page: Int): List<MovieEntity> {

        return items.map {
            val convertedGenres = GenreIdsTypeConverter.GenreIdData(
                data = it.genreIds
            )
            MovieEntity(
                id = it.id, title = it.title,
                overview = it.overview, language = it.originalLanguage,
                genreIds = convertedGenres, posterPath = it.posterPath, backdropPath = it.backdropPath,
                releaseDate = it.releaseDate, voteAverage = it.voteAverage, voteCount = it.voteCount, adult = it.adult,
                page = page
            )
        }
    }

    private suspend fun retrieveGenres() {
        if(!hasBeenUpdateGenre) {
            val retrieveFromApi = service.getMovieGenre(Config.TOKEN)
            val genres = retrieveFromApi.genres
            val mappedGenres = genres.map {
                Genres(it.id, it.name)
            }
            accessDb.genreDao().insertGenres(mappedGenres)
            hasBeenUpdateGenre = true
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieEntity>): RemoteKeysMovie? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let {
            val f = accessDb.remoteKeysMovieDao().remoteKeysMovieId(it.id.toLong())

            Log.e("REMOTEKEY", "GET ${it.id}. RESULT ${f?.nextKey}")
            f

        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieEntity>): RemoteKeysMovie? {
        return state.pages.firstOrNull {it.data.isNotEmpty()}?.data?.firstOrNull()?.let {
            val d = accessDb.remoteKeysMovieDao().remoteKeysMovieId(it.id.toLong())
            d
        }
    }

    private suspend fun getRemoteKeysClosestToCurrentPosition(state: PagingState<Int, MovieEntity>): RemoteKeysMovie? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let {
                accessDb.remoteKeysMovieDao().remoteKeysMovieId(it.toLong())
            }
        }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}


