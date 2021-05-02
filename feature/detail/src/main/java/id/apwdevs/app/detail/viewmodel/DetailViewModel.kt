package id.apwdevs.app.detail.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.detail.EpisodeToAir
import id.apwdevs.app.core.domain.usecase.DetailUseCase
import id.apwdevs.app.core.domain.usecase.FavoriteUseCase
import id.apwdevs.app.core.utils.State
import id.apwdevs.app.detail.data.EpisodeItemData
import id.apwdevs.app.detail.data.MovieDetail
import id.apwdevs.app.detail.data.TvShowDetail
import id.apwdevs.app.movieshow.MainApplication
import id.apwdevs.app.res.util.PageType
import id.apwdevs.app.res.util.zeroIfNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.floor

// these only from

@Deprecated("will use new implementation")
class DetailViewModel(
    application: Application,
    private val detailUseCase: DetailUseCase,
    private val favoriteUseCase: FavoriteUseCase
) : AndroidViewModel(application) {

    var itemId: Int = 0
    var itemData: Any? = null
    var pageType: PageType? = null

    // live data
    private val _favorited = MutableLiveData<Resource<Boolean>>()
    private val _data = MutableLiveData<Resource<Any>>()
    val favorited: LiveData<Resource<Boolean>> = _favorited
    val data: LiveData<Resource<Any>> = _data
    private val idling = (application as MainApplication).idlingRes


    fun loadData() {
        when (pageType) {
            PageType.MOVIES -> loadDetailMovie()
            PageType.TV_SHOW -> loadDetailTvShow()
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            if (_favorited.value !is Resource.Success) return@launch

            val currentFav = _favorited.value?.data ?: false
            _favorited.value = Resource.Loading()
            if (currentFav) {
                favoriteUseCase.unFavorite(itemId, pageType == PageType.MOVIES)
            } else {
                when (pageType) {
                    PageType.MOVIES -> favoriteUseCase.saveFavoriteMovie(itemData as DetailMovie)
                    PageType.TV_SHOW -> favoriteUseCase.saveFavoriteTvShow(itemData as DetailTvShow)
                }
            }

            // check favorite on the last
            val isFav = isInFavorite(itemId, pageType == PageType.MOVIES)
            _favorited.value = Resource.Success(isFav)
        }
    }

    private fun loadDetailMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            _favorited.postValue(Resource.Loading())
            _data.postValue(Resource.Loading())
            idling.increment()

            val onRoom = isInFavorite(itemId, true)
            _favorited.postValue(Resource.Success(onRoom))

            val data = if (onRoom) {
                favoriteUseCase.getFavoriteMovie(itemId)
            } else {
                detailUseCase.getDetailMovie(itemId)
            }

            try {
                data.collect {
                    itemData = it as DetailMovie
                    when(itemData){
                        is State.Error -> {}
                        is State.Loading -> {}
                        is State.Success<*> -> {}
                    }
                    val generated = MovieDetail(
                            originalLanguage = it.originalLanguage, title = it.title, backdropPath = it.backdropPath,
                            overview = it.overview, runTime = composeRuntime(it.runtime), posterPath = it.posterPath,
                            releaseDate = it.releaseDate, rating = it.voteAverage.toFloat(), tagline = it.tagline,
                            status = it.status, genres = it.genres
                    )

                    _data.postValue(Resource.Success(generated))
                }
            } catch (e: Throwable) {
                Log.e("ERROR", "ERror", e)
                e.printStackTrace()
                _data.postValue(Resource.Failed(e))
            }

            Log.e("HELLO", "MAIN CODE")
            idling.decrement()

        }
    }


    private fun loadDetailTvShow() {
        viewModelScope.launch {
            _favorited.value = Resource.Loading()
            _data.value = Resource.Loading()

            val onRoom = isInFavorite(itemId, false)
            _favorited.value = Resource.Success(onRoom)

            val data = if (onRoom) {
                favoriteUseCase.getFavoriteTvShow(itemId)
            } else {
                detailUseCase.getDetailTvShow(itemId)
            }

            try {
                data.collect {
                    itemData = it as DetailTvShow
                    val generated = TvShowDetail(
                            originalLanguage = it.originalLanguage, title = it.name, backdropPath = it.backdropPath,
                            overview = it.overview, posterPath = it.posterPath, firstAirDate = it.firstAirDate,
                            rating = it.voteAverage.toFloat(), tagline = it.tagline, status = it.status,
                            genres = it.genres, lastEpisodeToAir = composeEpisode(it.lastEpisodeToAir), nextEpisodeToAir = it.nextEpisodeToAir?.let { n -> composeEpisode(n) }?: null,
                            seasons = it.seasons, type = it.type
                    )

                    _data.value = Resource.Success(generated)
                }
            } catch (e: Throwable) {
                _data.value = Resource.Failed(e)
            }

        }
    }

    private fun composeEpisode(lastEpisodeToAir: EpisodeToAir): EpisodeItemData {
        return EpisodeItemData(
            lastEpisodeToAir.stillPath,
            lastEpisodeToAir.name,
            lastEpisodeToAir.voteAverage,
            lastEpisodeToAir.airDate,
            lastEpisodeToAir.seasonNumber
        )
    }

    private fun composeRuntime(runtime: Int?): String {
        val runTime = runtime.zeroIfNull
        return if (runTime == 0) "0h 0m"
        else {
            val hours = floor(runTime.toDouble() / 60).toInt()
            val minutes = runTime % 60

            "${hours}h ${minutes}m"
        }
    }

    private suspend fun isInFavorite(id: Int, isMovie: Boolean) =
        favoriteUseCase.checkIsInFavorite(id, isMovie)

    sealed class Resource<T>(val data: T?, val error: Throwable?) {
        class Loading<T>() : Resource<T>(null, null)
        class Success<T>(data: T) : Resource<T>(data, null)
        class Failed<T>(error: Throwable) : Resource<T>(null, error)

    }
}

