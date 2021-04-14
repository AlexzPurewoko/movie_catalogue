package id.apwdevs.app.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.paging.PagingData
import id.apwdevs.app.res.BaseFeatureFragment
import id.apwdevs.app.res.util.PageType
import id.apwdevs.app.search.adapter.SearchMovieShowAdapter
import id.apwdevs.app.search.databinding.FragmentSearchBinding
import id.apwdevs.app.search.di.searchModule
import id.apwdevs.app.search.model.SearchItem
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.module.Module
import ru.ldralighieri.corbind.widget.checkedChanges
import ru.ldralighieri.corbind.widget.itemSelections
import ru.ldralighieri.corbind.widget.textChanges

class SearchFragment : BaseFeatureFragment() {
    override val koinModules: List<Module>
        get() = listOf(searchModule)

    private lateinit var binding: FragmentSearchBinding
    private val viewPagerAdapter: SearchMovieShowAdapter by lazy {
        SearchMovieShowAdapter(this::anyClickFromItem)
    }

    private val searchVewModel: SearchVewModel by viewModel()
    internal val stateViewModel: StateViewModel by viewModels()
    private var referenceStateFragment: StateDisplayFragment? = null

    private var searchData: SearchVewModel.SearchData? = null

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
        stateViewModel.callbackFromStateDisplay.observe(
            viewLifecycleOwner,
            ::stateFragmentCallbackObserver
        )
    }

    private fun initAdapter() {
        viewPagerAdapter.addLoadStateListener { state ->

            val itemCount = viewPagerAdapter.itemCount
            when {
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
        stateViewModel.callStateFragmentToDisplaySomething(type)
    }

    private fun stateFragmentCallbackObserver(parameters: List<Any>) {
        if (parameters[0] == StateViewModel.StateCallType.RETRY)
            searchData?.let {
                searchVewModel.search(it)
            }
    }

    private fun initiateView() {
        binding.spinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            CONTENTS.map { getString(it) })
        binding.recyclerView.adapter = viewPagerAdapter

        referenceStateFragment = StateDisplayFragment()
        childFragmentManager.commit {
            add(binding.frameStatusContainer.id, StateDisplayFragment())
            referenceStateFragment?.let { show(it) }
        }

        searchData?.let {
            binding.spinner.setSelection(
                when (it.pageType) {
                    PageType.MOVIES -> 0
                    PageType.TV_SHOW -> 1
                }
            );

            binding.isAdult.isChecked = it.includeAdult
            binding.include.textSearch.setText(it.searchQuery)
        }

        stateViewModel.callStateFragmentToDisplaySomething(StateViewModel.DisplayType.RECOMMENDATION)
    }

    private fun toggleStateDisplayFragment(displayed: Boolean) {
        childFragmentManager.commit {
            referenceStateFragment?.let {
                if (displayed) show(it)
                else hide(it)
            }
        }
        binding.recyclerView.visibility = if (displayed) View.INVISIBLE else View.VISIBLE
    }

    private fun anyClickFromItem(item: SearchItem) {
        Toast.makeText(requireContext(), item.title, Toast.LENGTH_LONG).show()
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
