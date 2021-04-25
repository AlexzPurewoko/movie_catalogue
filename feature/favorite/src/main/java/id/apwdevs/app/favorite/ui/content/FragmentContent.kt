package id.apwdevs.app.favorite.ui.content

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import id.apwdevs.app.core.utils.State
import id.apwdevs.app.favorite.R
import id.apwdevs.app.favorite.adapter.FavoriteMovieShowAdapter
import id.apwdevs.app.favorite.databinding.FragmentContentBinding
import id.apwdevs.app.favorite.viewmodel.FavoriteViewModel
import id.apwdevs.app.res.data.MovieShowItem
import id.apwdevs.app.res.fragment.FragmentWithState
import id.apwdevs.app.res.fragment.viewmodel.StateViewModel
import id.apwdevs.app.res.util.PageType
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.module.Module

class FragmentContent : FragmentWithState() {

    private val favoriteViewModel: FavoriteViewModel by viewModel()

    private val binding: FragmentContentBinding by lazy {
        FragmentContentBinding.inflate(layoutInflater)
    }
    private val adapter: FavoriteMovieShowAdapter by lazy { FavoriteMovieShowAdapter(::onItemClick) }

    private val pageType: PageType? by lazy { arguments?.getParcelable(PAGE_TYPE_KEY) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        super.applyFragmentIntoView(binding.frameStatus.id)
        binding.recyclerView.adapter = adapter
        load()
    }

    private fun load() {
        when (pageType) {
            PageType.MOVIES -> favoriteViewModel.getFavoriteMovies()
            PageType.TV_SHOW -> favoriteViewModel.getFavoriteTvShows()
            else -> null
        }?.observe(viewLifecycleOwner, ::observeData)
    }

    private fun observeData(data: State<List<MovieShowItem>>) {
        var dataHidden = true
        when (data) {
            is State.Error -> callDisplayType(StateViewModel.DisplayType.ERROR)
            is State.Loading -> callDisplayType(StateViewModel.DisplayType.LOADING)
            is State.Success -> {
                if (data.data.isNullOrEmpty())
                    callDisplayType(StateViewModel.DisplayType.DATA_EMPTY)
                else {
                    data.data?.let { adapter.update(it) }
                    dataHidden = false
                }
            }
        }
        Log.e("JHE", "displayData: $data. toggleState: $dataHidden")
        super.toggleStateDisplayFragment(dataHidden)
        binding.frameStatus.visibility = if(dataHidden) View.VISIBLE else View.INVISIBLE
        binding.recyclerView.visibility = if(!dataHidden) View.VISIBLE else View.INVISIBLE
    }

    private fun callDisplayType(displayType: StateViewModel.DisplayType) {
        super.callStateFragmentToDisplaySomething(displayType)
    }

    private fun onItemClick(item: MovieShowItem) {
        Toast.makeText(requireContext(), "Item Click ${item.id}", Toast.LENGTH_LONG).show()

    }

    // overridden methods
    override fun mapOfTextDisplay(): HashMap<StateViewModel.DisplayType, Int> =
        hashMapOf(
            StateViewModel.DisplayType.DATA_EMPTY to R.string.no_data_favorite
        )

    override fun provideCallbackFromStateDisplay(parameters: List<Any>) {
        if (parameters[0] == StateViewModel.StateCallType.RETRY)
            load()
    }

    // we don't need to load another modules in this child fragment
    override val koinModules: List<Module>
        get() = listOf()

    companion object {

        private const val PAGE_TYPE_KEY: String = "PAGE_TYPE"

        @JvmStatic
        fun newInstance(pageType: PageType): FragmentContent =
            FragmentContent().apply {
                arguments = Bundle().also { it.putParcelable(PAGE_TYPE_KEY, pageType) }
            }
    }

}