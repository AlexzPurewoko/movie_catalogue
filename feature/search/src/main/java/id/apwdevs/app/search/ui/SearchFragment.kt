package id.apwdevs.app.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import id.apwdevs.app.detail.ui.DetailItemFragmentArgs
import id.apwdevs.app.movieshow.R.id.detailFragment
import id.apwdevs.app.res.fragment.FragmentWithState
import id.apwdevs.app.res.fragment.viewmodel.StateViewModel
import id.apwdevs.app.res.util.PageType
import id.apwdevs.app.search.R
import id.apwdevs.app.search.adapter.SearchMovieShowAdapter
import id.apwdevs.app.search.databinding.FragmentSearchBinding
import id.apwdevs.app.search.di.searchModule
import id.apwdevs.app.search.model.SearchItem
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.module.Module
import ru.ldralighieri.corbind.widget.checkedChanges
import ru.ldralighieri.corbind.widget.itemSelections
import ru.ldralighieri.corbind.widget.textChanges

@FlowPreview
class SearchFragment : FragmentWithState() {
    override val koinModules: List<Module>
        get() = listOf(searchModule)

    private lateinit var binding: FragmentSearchBinding
    private val viewPagerAdapter: SearchMovieShowAdapter by lazy {
        SearchMovieShowAdapter(this::anyClickFromItem)
    }

    private val searchVewModel: SearchVewModel by viewModel()

    private var searchData: SearchVewModel.SearchData? = null
    private var hasFirstInitialization: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        savedInstanceState?.let {
            searchData = it.getParcelable(SEARCH_DATA_PARCEL)
        }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SEARCH_DATA_PARCEL, searchData)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initiateView()
        initAdapter()

        searchVewModel.initViewInteractions(
            binding.include.textSearch.textChanges(),
            binding.isAdult.checkedChanges(),
            binding.spinner.itemSelections()
        ).observe(viewLifecycleOwner, this::listenInteractions)
    }

    private fun initAdapter() {
        viewPagerAdapter.addLoadStateListener { state ->

            val itemCount = viewPagerAdapter.itemCount
            when {
                !hasFirstInitialization -> {
                    callDisplay(StateViewModel.DisplayType.RECOMMENDATION)
                    hasFirstInitialization = true
                }
                state.refresh is LoadState.NotLoading && itemCount == 0 ->
                    callDisplay(StateViewModel.DisplayType.DATA_EMPTY)
                state.source.refresh is LoadState.Error ->
                    callDisplay(StateViewModel.DisplayType.ERROR)
                state.refresh is LoadState.Loading ->
                    callDisplay(StateViewModel.DisplayType.LOADING)
                else ->
                    toggleStateDisplayFragment(false)
            }
        }
    }

    private fun callDisplay(type: StateViewModel.DisplayType) {
        toggleStateDisplayFragment(true)
        callStateFragmentToDisplaySomething(type)
    }

    private fun stateFragmentCallbackObserver(parameters: List<Any>) {
        if (parameters[0] == StateViewModel.StateCallType.RETRY)
            searchData?.let {
                searchVewModel.search(it).observe(viewLifecycleOwner, ::searchResults)
            }
    }

    private fun initiateView() {
        binding.spinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            CONTENTS.map { getString(it) })
        binding.recyclerView.adapter = viewPagerAdapter

        super.applyFragmentIntoView(binding.frameStatusContainer.id)

        searchData?.let {
            binding.spinner.setSelection(
                when (it.pageType) {
                    PageType.MOVIES -> 0
                    PageType.TV_SHOW -> 1
                }
            )

            binding.isAdult.isChecked = it.includeAdult
            binding.include.textSearch.setText(it.searchQuery)
        }
        toggleStateDisplayFragment(true)
        callStateFragmentToDisplaySomething(StateViewModel.DisplayType.RECOMMENDATION)
    }

    override fun toggleStateDisplayFragment(displayed: Boolean) {
        super.toggleStateDisplayFragment(displayed)
        binding.recyclerView.visibility = if (displayed) View.GONE else View.VISIBLE
        binding.frameStatusContainer.visibility = if (displayed) View.VISIBLE else View.INVISIBLE
    }

    private fun anyClickFromItem(item: SearchItem) {
        Toast.makeText(requireContext(), item.title, Toast.LENGTH_LONG).show()
        searchData?.let {
            val bundle = DetailItemFragmentArgs(it.pageType, item.id).toBundle()
            findNavController().navigate(
                detailFragment, bundle
            )
        }

    }

    private fun listenInteractions(anyInteraction: Boolean) {
        if (anyInteraction) {
            val page = when (binding.spinner.selectedItemPosition) {
                0 -> PageType.MOVIES
                1 -> PageType.TV_SHOW
                else -> return
            }
            val query = binding.include.textSearch.text.toString()
            if (query.isEmpty()) return
            val includeAdult = binding.isAdult.isChecked

            val composeData = SearchVewModel.SearchData(query, page, includeAdult)
            searchVewModel.search(composeData)
                .observe(viewLifecycleOwner, ::searchResults)
            searchData = composeData
        }
    }

    override fun mapOfTextDisplay(): HashMap<StateViewModel.DisplayType, Int> = hashMapOf(
        StateViewModel.DisplayType.RECOMMENDATION to R.string.display_recommendation,
        StateViewModel.DisplayType.ERROR to R.string.display_error,
        StateViewModel.DisplayType.LOADING to R.string.search_loading,
        StateViewModel.DisplayType.DATA_EMPTY to R.string.display_empty
    )

    override fun provideCallbackFromStateDisplay(parameters: List<Any>) {
        stateFragmentCallbackObserver(parameters)
    }

    private fun searchResults(data: PagingData<SearchItem>) {
        viewPagerAdapter.submitData(lifecycle, data)
    }

    companion object {
        private val CONTENTS = intArrayOf(
            id.apwdevs.app.res.R.string.movies,
            id.apwdevs.app.res.R.string.tvshows
        )

        private const val SEARCH_DATA_PARCEL = "SEARCH_DATA"
    }
}
