package id.apwdevs.app.discover.ui.child

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import id.apwdevs.app.detail.ui.DetailItemFragmentArgs
import id.apwdevs.app.discover.R
import id.apwdevs.app.discover.databinding.FragmentMovieShowBinding
import id.apwdevs.app.res.adapter.ListMovieShowAdapter
import id.apwdevs.app.res.data.MovieShowItem
import id.apwdevs.app.res.fragment.FragmentWithState
import id.apwdevs.app.res.fragment.viewmodel.StateViewModel
import id.apwdevs.app.res.util.PageType
import id.apwdevs.app.res.util.changeStateDisplay
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.module.Module
import id.apwdevs.app.movieshow.R as BaseR

class MovieShowFragment : FragmentWithState() {

    private val movieShowViewModel: MovieShowViewModel by viewModel()
    private var movieShowAdapter: ListMovieShowAdapter? = null
    private var currentFragmentType: PageType? = null // by lazy { requireArguments().getParcelable(SHOWS_TYPE)!! }

    private var binding: FragmentMovieShowBinding? = null
    private var storeLoadStateFragment: ((CombinedLoadStates) -> Unit)? = null

    override val koinModules: List<Module>
        get() = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieShowBinding.inflate(inflater, container, false)
        currentFragmentType = arguments?.getParcelable(SHOWS_TYPE)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieShowAdapter = ListMovieShowAdapter(this::onItemClicked)

        binding?.apply {
            recyclerView.adapter = movieShowAdapter

            currentFragmentType?.let {
                movieShowViewModel.discoverPopular(it)
                    .observe(viewLifecycleOwner, this@MovieShowFragment::observingData)
            }

            applyFragmentIntoView(frameStatus.id)
            initAdapter()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        storeLoadStateFragment?.let { movieShowAdapter?.removeLoadStateListener(it) }
        binding?.recyclerView?.adapter = null
        storeLoadStateFragment = null
        currentFragmentType = null
        movieShowAdapter = null
        binding = null
    }

    private fun initAdapter() {
        storeLoadStateFragment = { state ->
            val itemCount = movieShowAdapter?.itemCount
            when {

                state.refresh is LoadState.NotLoading && itemCount == 0 ->
                    callDisplay(StateViewModel.DisplayType.DATA_EMPTY)
                state.mediator?.refresh is LoadState.Error -> {
                    callDisplay(StateViewModel.DisplayType.ERROR)
                }
                state.refresh is LoadState.Loading && state.mediator?.refresh is LoadState.Loading ->
                    callDisplay(StateViewModel.DisplayType.LOADING)

                else ->
                    toggleStateDisplayFragment(false)

            }

        }

        storeLoadStateFragment?.let { movieShowAdapter?.addLoadStateListener(it) }


    }

    private fun callDisplay(displayType: StateViewModel.DisplayType) {
        toggleStateDisplayFragment(true)
        callStateFragmentToDisplaySomething(displayType)
    }

    override fun mapOfTextDisplay(): HashMap<StateViewModel.DisplayType, Int> = hashMapOf(
        StateViewModel.DisplayType.DATA_EMPTY to R.string.discover_data_empty,
        StateViewModel.DisplayType.ERROR to R.string.discover_data_error,
        StateViewModel.DisplayType.LOADING to R.string.discover_data_loading
    )

    override fun toggleStateDisplayFragment(displayed: Boolean) {
        super.toggleStateDisplayFragment(displayed)
        binding?.recyclerView?.changeStateDisplay(!displayed)
        binding?.frameStatus?.changeStateDisplay(displayed)
    }

    @Synchronized
    override fun provideCallbackFromStateDisplay(parameters: List<Any>) {
        if (parameters[0] === StateViewModel.StateCallType.RETRY && currentFragmentType != null) {
            /**
             * Add double bang because i ensure that's not null from last direct-checking
             */
            movieShowViewModel.discoverPopular(currentFragmentType!!)
                .observe(viewLifecycleOwner, this::observingData)
        }
    }


    private fun observingData(data: PagingData<MovieShowItem>) {
        movieShowAdapter?.submitData(lifecycle, data)
    }

    private fun onItemClicked(data: MovieShowItem) {
        Toast.makeText(requireContext(), "Item Click ${data.id}", Toast.LENGTH_LONG).show()
        currentFragmentType?.let {
            val bundle = DetailItemFragmentArgs(it, data.id).toBundle()
            findNavController().navigate(
                BaseR.id.detailFragment, bundle
            )
        }
    }

    companion object {

        const val SHOWS_TYPE: String = "SHOWS_FRAGMENT_TYPE"

        fun newInstance(type: PageType): MovieShowFragment {
            return MovieShowFragment().apply {
                arguments = Bundle().also { it.putParcelable(SHOWS_TYPE, type) }
            }
        }

    }

}