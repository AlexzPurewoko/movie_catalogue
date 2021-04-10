package id.apwdevs.app.data.source.local.paging

import androidx.paging.PagingSource
import id.apwdevs.app.data.source.local.entity.detail.tvshow.FavDetailTvShowEntity
import id.apwdevs.app.data.source.local.room.AppDatabase

@Deprecated("will be deleted, for favorite use only with flow, not paging")
class TvShowPagingSource(
        private val accessDb: AppDatabase
): PagingSource<Int, FavDetailTvShowEntity>() {

    private var countOffsetItem = 0
    private var startOffsetItem = 0
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FavDetailTvShowEntity> {
        val position = params.key ?: 1
        val response = accessDb.favTvShowDetail().getTvShowEntity(startOffsetItem, params.loadSize)
        val nextKey = if(response.isEmpty()) {
            null
        } else {
            position + (params.loadSize/LOAD_SIZE)
        }
        countOffsetItem = response.size
        startOffsetItem += countOffsetItem
        return LoadResult.Page(
                data = response,
                prevKey = if(position == 1) null else position - 1,
                nextKey = nextKey
        )
    }

    companion object {
        const val LOAD_SIZE = 10
    }
}