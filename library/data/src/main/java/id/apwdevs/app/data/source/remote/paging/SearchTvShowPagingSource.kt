package id.apwdevs.app.data.source.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import id.apwdevs.app.data.source.remote.network.TvShowsNetwork
import id.apwdevs.app.data.source.remote.response.TvShowItemResponse
import retrofit2.HttpException
import java.io.IOException

class SearchTvShowPagingSource(
    private val tvShowsNetwork: TvShowsNetwork,
    private val query: String,
    private val includeAdult: Boolean = false
) : PagingSource<Int, TvShowItemResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TvShowItemResponse> {
        val position = params.key ?: 1
        return try {
            val response = tvShowsNetwork.searchTvShows(query, includeAdult, position)
            val results =
                response.results.filter { it.name.isNotEmpty() && !it.firstAirDate.isNullOrEmpty() }
            val totalPages = response.totalPages
            var nextKey: Int? = position + (params.loadSize / ITEM_PER_PAGE)
            nextKey =
                if (nextKey!! > totalPages)
                    null
                else nextKey


            LoadResult.Page(
                data = results,
                prevKey = if (position == 1) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override val keyReuseSupported: Boolean
        get() = true

    override fun getRefreshKey(state: PagingState<Int, TvShowItemResponse>): Int? {

        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        const val ITEM_PER_PAGE = 20
    }
}