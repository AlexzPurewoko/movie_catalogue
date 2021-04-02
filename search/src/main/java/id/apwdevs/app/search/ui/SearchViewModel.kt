package id.apwdevs.app.search.ui

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.map
import id.apwdevs.app.core.domain.usecase.SearchUseCase
import id.apwdevs.app.res.util.PageType
import id.apwdevs.app.search.databinding.FragmentSearchBinding
import id.apwdevs.app.search.model.SearchItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.map
import ru.ldralighieri.corbind.widget.checkedChanges
import ru.ldralighieri.corbind.widget.itemSelections
import ru.ldralighieri.corbind.widget.textChanges

class SearchVewModel(
    private val searchUseCase: SearchUseCase
): ViewModel() {

    private var currentSearch: String = ""
    private var savedPosition: Int = 0
    private val _listenInteractons: MutableLiveData<Boolean> = MutableLiveData()
    //    var listenInteractions: LiveData<Boolean> = _listenInteractons
    fun search(query: String, pageType: PageType, includeAdult: Boolean): LiveData<PagingData<SearchItem>> =
        when(pageType) {
            PageType.MOVIES ->
                searchMovies(query, includeAdult)
            PageType.TV_SHOW ->
                searchTvShow(query, includeAdult)
        }.asLiveData(viewModelScope.coroutineContext)

    fun initiateViewInteractions(binding: FragmentSearchBinding) : LiveData<Boolean> {
        val textSearchChanges = binding.include.textSearch.textChanges()
        val isAdultCheckChanges = binding.isAdult.checkedChanges()
        val spinnerChanges = binding.spinner.itemSelections()

        combine(
            textSearchChanges.map {
                if(it.toString().isNotEmpty() && currentSearch != it.toString()){
                    currentSearch = it.toString()
                    true
                }
                else false
            },
            isAdultCheckChanges,
            spinnerChanges.map {
                if(it != savedPosition){
                    savedPosition = it
                    true
                }
                else false
            },
            transform = { anyTextChanges, anyCheckedChanges, anyItemSpinnerChanges ->
                anyTextChanges or anyCheckedChanges or anyItemSpinnerChanges
            }
        ).onEach(this::applyInteractions).launchIn(viewModelScope)
        return _listenInteractons
    }

    private fun applyInteractions(anyInteractions: Boolean){
        _listenInteractons.value = anyInteractions
    }
    private fun searchMovies(query: String, includeAdult: Boolean): Flow<PagingData<SearchItem>> =
        searchUseCase.searchMovie(query, includeAdult)
            .map { pagingData -> pagingData.map {
                SearchItem(it.movieId, it.title, it.backdropPath, it.voteAverage, it.releaseDate)
            }}

    private fun searchTvShow(query: String, includeAdult: Boolean): Flow<PagingData<SearchItem>> =
        searchUseCase.searchTvShow(query, false)
            .map { pagingData -> pagingData.map {
                SearchItem(it.tvId, it.name, it.backdropPath, it.voteAverage, it.firstAirDate)
            } }
}