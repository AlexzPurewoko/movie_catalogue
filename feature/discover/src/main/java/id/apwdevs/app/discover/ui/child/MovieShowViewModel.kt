package id.apwdevs.app.discover.ui.child

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import id.apwdevs.app.core.domain.usecase.DiscoverPopularUseCase
import id.apwdevs.app.movieshow.base.BaseViewModel
import id.apwdevs.app.res.data.MovieShowItem
import id.apwdevs.app.res.util.PageType
import id.apwdevs.app.res.util.mapToItem
import kotlinx.coroutines.flow.map

class MovieShowViewModel(
    application: Application,
    private val popularUseCase: DiscoverPopularUseCase
) : BaseViewModel(application) {


    fun discoverPopular(type: PageType): LiveData<PagingData<MovieShowItem>> =
        when (type) {
            PageType.MOVIES -> displayPopularMovie()
            PageType.TV_SHOW -> displayPopularTvShow()
        }


    private fun displayPopularMovie(): LiveData<PagingData<MovieShowItem>> {
        return popularUseCase.discoverPopularMovies()
            .map { pagingData ->
                pagingData.map {
                    it.mapToItem()
                }
            }.toLiveData(viewModelScope.coroutineContext)
    }

    private fun displayPopularTvShow(): LiveData<PagingData<MovieShowItem>> {
        return popularUseCase.discoverPopularTvShow()
            .map { pagingData ->
                pagingData.map {
                    it.mapToItem()
                }
            }.toLiveData(viewModelScope.coroutineContext)
    }
}