package id.apwdevs.app.discover.ui.child

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
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
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.module.Module
import id.apwdevs.app.movieshow.R as BaseR

class MovieShowFragment : FragmentWithState() {

    private val movieShowViewModel: MovieShowViewModel by viewModel()
    private val movieShowAdapter: ListMovieShowAdapter by lazy { ListMovieShowAdapter(this::onItemClicked) }
    private val currentFragmentType: PageType by lazy { requireArguments().getParcelable(SHOWS_TYPE)!! }

    private lateinit var binding: FragmentMovieShowBinding

    override val koinModules: List<Module>
        get() = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = movieShowAdapter
        movieShowViewModel.discoverPopular(currentFragmentType)
            .observe(viewLifecycleOwner, this::observingData)

        applyFragmentIntoView(binding.frameStatus.id)
        initAdapter()

    }

    private fun initAdapter() {

        // will add removeload statelistener next
        movieShowAdapter.addLoadStateListener { state ->
            val itemCount =  movieShowAdapter.itemCount
            when {

                state.refresh is LoadState.NotLoading && itemCount == 0 ->
                    callDisplay(StateViewModel.DisplayType.DATA_EMPTY)
                state.mediator?.refresh is LoadState.Error ->{
                    val o = state.mediator?.refresh as? LoadState.Error
                    Log.e("MEDIATOR ERROR", "ERROR MESSAGE", o?.error)
                    callDisplay(StateViewModel.DisplayType.ERROR)
                }
                state.refresh is LoadState.Loading && state.mediator?.refresh is LoadState.Loading ->
                    callDisplay(StateViewModel.DisplayType.LOADING)

                else ->
                    toggleStateDisplayFragment(false)

            }

            Log.e("STATE", "state: ${state.refresh}, mediator: ${state.mediator?.refresh}")
        }

    }

    private fun callDisplay(displayType: StateViewModel.DisplayType) {
        toggleStateDisplayFragment(true)
        callStateFragmentToDisplaySomething(displayType)
    }

    override fun mapOfTextDisplay(): HashMap<StateViewModel.DisplayType, Int>  = hashMapOf(
        StateViewModel.DisplayType.DATA_EMPTY to R.string.discover_data_empty,
        StateViewModel.DisplayType.ERROR to R.string.discover_data_error,
        StateViewModel.DisplayType.LOADING to R.string.discover_data_loading
    )

    override fun toggleStateDisplayFragment(displayed: Boolean) {
        super.toggleStateDisplayFragment(displayed)
        binding.recyclerView.visibility = if (displayed) View.GONE else View.VISIBLE
        binding.frameStatus.visibility = if (displayed) View.VISIBLE else View.INVISIBLE
    }

    override fun provideCallbackFromStateDisplay(parameters: List<Any>) {
        if(parameters[0] === StateViewModel.StateCallType.RETRY){
            movieShowViewModel.discoverPopular(currentFragmentType)
                .observe(viewLifecycleOwner, this::observingData)
        }
    }


    private fun observingData(data: PagingData<MovieShowItem>) {
        movieShowAdapter.submitData(lifecycle, data)
    }

    private fun onItemClicked(data: MovieShowItem) {
        Toast.makeText(requireContext(), "Item Click ${data.id}", Toast.LENGTH_LONG).show()
        currentFragmentType.let {
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