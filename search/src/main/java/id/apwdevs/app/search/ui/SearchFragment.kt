package id.apwdevs.app.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.paging.PagingData
import id.apwdevs.app.res.BaseFeatureFragment
import id.apwdevs.app.res.util.PageType
import id.apwdevs.app.search.adapter.SearchMovieShowAdapter
import id.apwdevs.app.search.databinding.FragmentSearchBinding
import id.apwdevs.app.search.di.searchModule
import id.apwdevs.app.search.model.SearchItem
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.module.Module

class SearchFragment : BaseFeatureFragment() {
    override val koinModules: List<Module>
        get() = listOf(searchModule)

    private lateinit var binding: FragmentSearchBinding
    private val viewPagerAdapter: SearchMovieShowAdapter by lazy {
        SearchMovieShowAdapter(this::anyClickFromItem)
    }

    private val searchVewModel: SearchVewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, CONTENTS.map { getString(it) })
        binding.recyclerView.adapter = viewPagerAdapter

        searchVewModel.initiateViewInteractions(binding).observe(viewLifecycleOwner, this::listenInteractions)

    }

    private fun anyClickFromItem(item: SearchItem){
        Toast.makeText(requireContext(), item.title, Toast.LENGTH_LONG).show()
    }

    private fun listenInteractions(anyInteraction: Boolean){
        if(anyInteraction) {
            val page = when(binding.spinner.selectedItemPosition) {
                0 -> PageType.MOVIES
                1 -> PageType.TV_SHOW
                else -> return
            }
            val query = binding.include.textSearch.text.toString()
            if(query.isEmpty()) return
            val includeAdult = binding.isAdult.isChecked
            searchVewModel.search(query, page, includeAdult).observe(viewLifecycleOwner, this::searchResults)
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
    }
}
