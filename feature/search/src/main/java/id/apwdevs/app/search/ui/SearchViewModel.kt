package id.apwdevs.app.search.ui

import android.app.Application
import android.os.Parcelable
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.map
import id.apwdevs.app.core.domain.usecase.SearchUseCase
import id.apwdevs.app.movieshow.base.BaseViewModel
import id.apwdevs.app.res.util.PageType
import id.apwdevs.app.search.databinding.FragmentSearchBinding
import id.apwdevs.app.search.model.SearchItem
import kotlinx.coroutines.flow.*
import kotlinx.parcelize.Parcelize
import ru.ldralighieri.corbind.internal.InitialValueFlow
import ru.ldralighieri.corbind.widget.checkedChanges
import ru.ldralighieri.corbind.widget.itemSelections
import ru.ldralighieri.corbind.widget.textChanges

class SearchVewModel(
    application: Application,
    private val searchUseCase: SearchUseCase
) : BaseViewModel(application) {

    private var currentSearch: String = ""
    private var savedSearchParameters: SearchData? = null
    private var savedPosition: Int = 0
    private val _listenInteractons: MutableLiveData<Boolean> = MutableLiveData()
    private var _searchResults: LiveData<PagingData<SearchItem>>? = null
    //    var listenInteractions: LiveData<Boolean> = _listenInteractons

    @Deprecated("will be deleted!")
    fun search(
        query: String,
        pageType: PageType,
        includeAdult: Boolean
    ): LiveData<PagingData<SearchItem>> =
        when (pageType) {
            PageType.MOVIES ->
                searchMovies(query, includeAdult)
            PageType.TV_SHOW ->
                searchTvShow(query, includeAdult)
        }.asLiveData(viewModelScope.coroutineContext)

    fun search(
        searchParameter: SearchData
    ): LiveData<PagingData<SearchItem>> {
        if (savedSearchParameters == searchParameter && _searchResults != null) {
            return _searchResults as LiveData<PagingData<SearchItem>>
        }
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

    @Deprecated("Will be deleted!")
    fun initiateViewInteractions(binding: FragmentSearchBinding): LiveData<Boolean> {
        val textSearchChanges = binding.include.textSearch.textChanges()
        val isAdultCheckChanges = binding.isAdult.checkedChanges()
        val spinnerChanges = binding.spinner.itemSelections()

        combine(
            textSearchChanges.map {
                if (it.toString().isNotEmpty() && currentSearch != it.toString()) {
                    currentSearch = it.toString()
                    true
                } else false
            },
            isAdultCheckChanges,
            spinnerChanges.map {
                if (it != savedPosition) {
                    savedPosition = it
                    true
                } else false
            },
            transform = { anyTextChanges, anyCheckedChanges, anyItemSpinnerChanges ->
                anyTextChanges or anyCheckedChanges or anyItemSpinnerChanges
            }
        ).onEach(this::applyInteractions).launchIn(viewModelScope)
        return _listenInteractons
    }

    fun initViewInteractions(
        textSearchChanges: InitialValueFlow<CharSequence>,
        adultCheckChanges: InitialValueFlow<Boolean>,
        spinnerChanges: InitialValueFlow<Int>
    ): LiveData<Boolean> {

        combine(
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
        ).onEach(this::applyInteractions).launchIn(viewModelScope)

        return _listenInteractons
    }

    private fun applyInteractions(anyInteractions: Boolean) {
        _listenInteractons.value = anyInteractions
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

    @Parcelize
    data class SearchData(
        val searchQuery: String,
        val pageType: PageType,
        val includeAdult: Boolean
    ) : Parcelable
}