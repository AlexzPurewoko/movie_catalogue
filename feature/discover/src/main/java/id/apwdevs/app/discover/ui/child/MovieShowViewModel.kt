package id.apwdevs.app.discover.ui.child

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import id.apwdevs.app.core.domain.usecase.DiscoverPopularUseCase
import id.apwdevs.app.res.data.MovieShowItem
import id.apwdevs.app.res.util.PageType
import kotlinx.coroutines.flow.map

class MovieShowViewModel(
    private val popularUseCase: DiscoverPopularUseCase
) : ViewModel() {


    fun discoverPopular(type: PageType): LiveData<PagingData<MovieShowItem>> =
        when (type) {
            PageType.MOVIES -> displayPopularMovie()
            PageType.TV_SHOW -> displayPopularTvShow()
        }


    private fun displayPopularMovie(): LiveData<PagingData<MovieShowItem>> {
        return popularUseCase.discoverPopularMovies()
            .map { pagingData ->
                pagingData.map {
                    MovieShowItem(
                        it.movieId,
                        it.title,
                        it.overview,
                        it.backdropPath,
                        it.genres,
                        it.voteCount,
                        it.voteAverage
                    )
                }
            }.asLiveData(viewModelScope.coroutineContext)
    }

    private fun displayPopularTvShow(): LiveData<PagingData<MovieShowItem>> {
        return popularUseCase.discoverPopularTvShow()
            .map { pagingData ->
                pagingData.map {
                    MovieShowItem(
                        it.tvId,
                        it.name,
                        it.overview,
                        it.backdropPath,
                        it.genres,
                        it.voteCount,
                        it.voteAverage
                    )
                }
            }.asLiveData(viewModelScope.coroutineContext)
    }
}