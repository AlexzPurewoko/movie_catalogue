package id.apwdevs.app.core.utils

import androidx.paging.PagingConfig

object Config {
    fun pageConfig() = PagingConfig(
        pageSize = 20,
        prefetchDistance = 3,
        initialLoadSize = 20,
        enablePlaceholders = false
    )
}