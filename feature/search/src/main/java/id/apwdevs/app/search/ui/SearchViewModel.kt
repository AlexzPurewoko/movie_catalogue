package id.apwdevs.app.search.ui

import android.app.Application
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import id.apwdevs.app.core.domain.usecase.SearchUseCase
import id.apwdevs.app.movieshow.base.BaseViewModel
import id.apwdevs.app.res.util.PageType
import id.apwdevs.app.search.model.SearchItem
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.parcelize.Parcelize
import ru.ldralighieri.corbind.internal.InitialValueFlow

class SearchVewModel(
    application: Application,
    private val searchUseCase: SearchUseCase
) : BaseViewModel(application) {

    private var currentSearch: String = ""
    private var savedSearchParameters: SearchData? = null
    private var savedPosition: Int = 0
    private val _listenInteractions: MutableLiveData<Boolean> = MutableLiveData()
    private var _searchResults: LiveData<PagingData<SearchItem>>? = null

    private var refCombineJob: Job? = null

    @FlowPreview
    fun search(
        searchParameter: SearchData,
        forceReload: Boolean = false
    ): LiveData<PagingData<SearchItem>> {

        if(!forceReload && searchParameter == savedSearchParameters && _searchResults != null)
            return _searchResults!!

        val (query, pageType, includeAdult) = searchParameter
        savedSearchParameters = searchParameter
        _searchResults = when (pageType) {
            PageType.MOVIES ->
                searchMovies(query, includeAdult)
            PageType.TV_SHOW ->
                searchTvShow(query, includeAdult)
        }.toLiveData(viewModelScope.coroutineContext)

        return _searchResults as LiveData<PagingData<SearchItem>>
    }

    @FlowPreview
    fun initViewInteractions(
        textSearchChanges: InitialValueFlow<CharSequence>,
        adultCheckChanges: InitialValueFlow<Boolean>,
        spinnerChanges: InitialValueFlow<Int>
    ): LiveData<Boolean> {

        refCombineJob = combine(
            textSearchChanges.map {
                if (it.toString().isNotEmpty() && currentSearch != it.toString()) {
                    currentSearch = it.toString()
                    true
                } else false
            },
            adultCheckChanges,
            spinnerChanges.map {
                if (it != savedPosition) {
                    savedPosition = it
                    true
                } else false
            },
            transform = { anyTextChanges, anyCheckedChanges, anyItemSpinnerChanges ->
                anyTextChanges or anyCheckedChanges or anyItemSpinnerChanges
            }
        )
            .debounce(300)
            .onEach(::applyInteractions).launchIn(viewModelScope)

        return _listenInteractions
    }

    private fun applyInteractions(anyInteractions: Boolean) {
        _listenInteractions.value = anyInteractions
    }

    private fun searchMovies(query: String, includeAdult: Boolean): Flow<PagingData<SearchItem>> =
        searchUseCase.searchMovie(query, includeAdult)
            .map { pagingData ->
                pagingData.map {
                    SearchItem(
                        it.movieId,
                        it.title,
                        it.backdropPath,
                        it.voteAverage,
                        it.releaseDate
                    )
                }
            }

    private fun searchTvShow(query: String, includeAdult: Boolean): Flow<PagingData<SearchItem>> =
        searchUseCase.searchTvShow(query, includeAdult)
            .map { pagingData ->
                pagingData.map {
                    SearchItem(it.tvId, it.name, it.backdropPath, it.voteAverage, it.firstAirDate)
                }
            }

    fun clearJob() {
        refCombineJob?.cancel()
        refCombineJob = null
    }


    override fun onCleared() {
        clearJob()
    }

    @Parcelize
    data class SearchData(
        val searchQuery: String,
        val pageType: PageType,
        val includeAdult: Boolean
    ) : Parcelable
}