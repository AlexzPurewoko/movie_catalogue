package id.apwdevs.app.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import id.apwdevs.app.data.source.remote.response.MovieItemResponse
import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.local.entity.RemoteKeysMovie
import id.apwdevs.app.data.source.local.entity.converters.GenreIdsTypeConverter
import id.apwdevs.app.data.source.local.entity.items.MovieEntity
import id.apwdevs.app.data.source.local.room.AppDatabase
import id.apwdevs.app.data.source.remote.service.ApiService
import id.apwdevs.app.data.utils.Config
import id.apwdevs.app.data.utils.QueryType
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val service: ApiService,
    private val accessDb: AppDatabase,
    private val queryType: QueryType
): RemoteMediator<Int, MovieEntity>() {

    private var hasBeenUpdateGenre: Boolean = false

    override suspend fun load(loadType: LoadType, state: PagingState<Int, MovieEntity>): MediatorResult {

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
            val apiResponse = when(queryType){
                is QueryType.SEARCH -> service.searchMovies(Config.TOKEN, page, queryType.query, queryType.includeAdult)
                is QueryType.POPULAR -> service.getPopularMovies(Config.TOKEN, "en-US", page)
            }
            val items = apiResponse.results
            val endOfPaginationReached = items.isEmpty()
            accessDb.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    accessDb.remoteKeysMovieDao().clearRemoteKeys()
                    accessDb.movieDao().clearMovies()
                }

                val prevKey = if(page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if(endOfPaginationReached) null else page + 1
                val keys = items.map {
                    RemoteKeysMovie(movieId = it.id.toLong(), prevKey = prevKey, nextKey = nextKey)
                }
                accessDb.remoteKeysMovieDao().insertAll(keys)

                val mappedMoviesToEntity = mapMoviesToEntity(items)
                accessDb.movieDao().insertMovie(mappedMoviesToEntity)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException){
            return MediatorResult.Error(e)
        } catch (e: HttpException){
            return MediatorResult.Error(e)
        }
    }

    private fun mapMoviesToEntity(items: List<MovieItemResponse>): List<MovieEntity> {

        return items.map {
            val convertedGenres = GenreIdsTypeConverter.GenreIdData(
                data = it.genreIds
            )
            MovieEntity(
                id = it.id, title = it.title,
                overview = it.overview, language = it.originalLanguage,
                genreIds = convertedGenres, posterPath = it.posterPath, backdropPath = it.backdropPath,
                releaseDate = it.releaseDate, voteAverage = it.voteAverage, voteCount = it.voteCount, adult = it.adult
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
            accessDb.remoteKeysMovieDao().remoteKeysMovieId(it.id.toLong())
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieEntity>): RemoteKeysMovie? {
        return state.pages.firstOrNull {it.data.isNotEmpty()}?.data?.firstOrNull()?.let {
            accessDb.remoteKeysMovieDao().remoteKeysMovieId(it.id.toLong())
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