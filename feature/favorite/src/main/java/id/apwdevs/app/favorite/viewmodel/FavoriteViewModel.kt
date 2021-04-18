package id.apwdevs.app.favorite.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import id.apwdevs.app.core.domain.usecase.FavUseCase
import id.apwdevs.app.core.utils.State
import id.apwdevs.app.favorite.util.Mapper
import id.apwdevs.app.movieshow.base.BaseViewModel
import id.apwdevs.app.res.data.MovieShowItem
import kotlinx.coroutines.flow.map

class FavoriteViewModel(
    application: Application,
    private val favoriteUseCase: FavUseCase,
) : BaseViewModel(application) {

    fun getFavoriteMovies(): LiveData<State<List<MovieShowItem>>> =
        favoriteUseCase.getAllFavoriteMovies()
            .map {
                it.copyTo { list ->
                    list.map {mov -> Mapper.mapDomainMovieToMovieShowItem(mov) }
                }
            }
            .toLiveData(viewModelScope.coroutineContext)

    fun getFavoriteTvShows(): LiveData<State<List<MovieShowItem>>> =
        favoriteUseCase.getAllFavoriteTvShows()
            .map {
                it.copyTo { list ->
                    list.map { tvShow -> Mapper.mapDomainTvShowToMovieShowItem(tvShow) }
                }
            }
            .toLiveData(viewModelScope.coroutineContext)

}