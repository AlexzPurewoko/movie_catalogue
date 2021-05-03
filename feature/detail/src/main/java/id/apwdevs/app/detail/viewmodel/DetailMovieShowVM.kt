package id.apwdevs.app.detail.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.usecase.DetailUseCase
import id.apwdevs.app.core.domain.usecase.FavUseCase
import id.apwdevs.app.core.utils.DataType
import id.apwdevs.app.core.utils.State
import id.apwdevs.app.detail.data.MovieDetail
import id.apwdevs.app.detail.data.TvShowDetail
import id.apwdevs.app.detail.util.mapToItem
import id.apwdevs.app.movieshow.base.BaseViewModel
import id.apwdevs.app.res.util.PageType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

class DetailMovieShowVM(
    application: Application,
    private val detailUseCase: DetailUseCase,
    private val favoriteUseCase: FavUseCase
) : BaseViewModel(application) {
    var itemId: Int = 0
    var itemData: Any? = null
    var pageType: PageType? = null

    var isFavorited: Boolean = false

    private val _data = MutableLiveData<State<DataPostType>>()
    val data: LiveData<State<DataPostType>> = _data


    fun loadData() {
        when(pageType){
            PageType.MOVIES -> loadDetailMovie()
            PageType.TV_SHOW -> loadDetailTvShow()
            null -> throw NullPointerException("pageType cannot be null!")
        }
    }

    fun toggleFavorite() {
        safelyExecuteTask(Dispatchers.IO) {
            // if any progress performed, then return
            if(_data.value is State.Loading) return@safelyExecuteTask
            _data.postValue(State.Loading(FAVORITE_LOADING_TAG))

            if(isFavorited){
                favoriteUseCase.unFavorite(itemId, when(pageType){
                    PageType.MOVIES -> DataType.MOVIES
                    PageType.TV_SHOW -> DataType.TVSHOW
                    null -> throw NullPointerException("pageType cannot be null!")
                })
            } else {
                when(pageType) {
                    PageType.MOVIES -> favoriteUseCase.saveFavoriteMovie(itemData as DetailMovie)
                    PageType.TV_SHOW -> favoriteUseCase.saveFavoriteTvShow(itemData as DetailTvShow)
                }
            }

            isFavorited = favoriteUseCase.checkIsInFavorite(itemId, when(pageType){
                PageType.MOVIES -> DataType.MOVIES
                PageType.TV_SHOW -> DataType.TVSHOW
                null -> throw NullPointerException("pageType cannot be null!")
            })

            _data.postValue(State.Success(
                DataPostType(PostType.FAVORITE_STATE, isFavorited)
            ))
        }
    }

    private fun loadDetailTvShow() {
        safelyExecuteTask(Dispatchers.IO) {
            _data.postValue(State.Loading())
            val isInFavaorite = favoriteUseCase.checkIsInFavorite(itemId, DataType.TVSHOW)
            _data.postValue(State.Success(
                    DataPostType(PostType.FAVORITE_STATE, isInFavaorite)
            ))
            isFavorited = isInFavaorite

            val data = if(isInFavaorite){
                favoriteUseCase.getFavoriteTvShow(itemId)
            } else {
                detailUseCase.getDetailTvShow(itemId)
            }

            var mappedData: TvShowDetail? = null
            try {
                data.collect { retData ->
                    when (retData){
                        is State.Error -> retData.error?.let {
                            throw it
                        }
                        is State.Loading -> {}
                        is State.Success -> {
                            itemData = retData.data
                            mappedData = retData.data?.mapToItem()
                        }
                    }
                }
            } catch (e: Throwable){

                _data.postValue(State.Error(e))
                return@safelyExecuteTask
            }

            _data.postValue(State.Success(
                    DataPostType(PostType.DATA, mappedData)
            ))

        }
    }

    private fun loadDetailMovie() {
        safelyExecuteTask(Dispatchers.IO) {
            _data.postValue(State.Loading())
            val isInFavaorite = favoriteUseCase.checkIsInFavorite(itemId, DataType.MOVIES)
            _data.postValue(State.Success(
                    DataPostType(PostType.FAVORITE_STATE, isInFavaorite)
            ))

            isFavorited = isInFavaorite

            val data = if(isInFavaorite){
                favoriteUseCase.getFavoriteMovie(itemId)
            } else {
                detailUseCase.getDetailMovie(itemId)
            }

            var mappedData: MovieDetail? = null
            try {
                data.collect { retData ->
                    when (retData){
                        is State.Error -> retData.error?.let {
                            throw it
                        }
                        is State.Loading -> {}
                        is State.Success -> {
                            itemData = retData.data
                            mappedData = retData.data?.mapToItem()
                        }
                    }
                }
            } catch (e: Throwable){

                _data.postValue(State.Error(e))
                return@safelyExecuteTask
            }

            _data.postValue(State.Success(
                DataPostType(PostType.DATA, mappedData)
            ))

        }
    }

    companion object {
        const val FAVORITE_LOADING_TAG = "favloading"
    }

    data class DataPostType(
        val postType: PostType,
        val data: Any?
    )

    enum class PostType {
        DATA,
        FAVORITE_STATE
    }
}

