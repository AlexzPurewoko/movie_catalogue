package id.apwdevs.app.discover.ui.child

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.paging.PagingData
import id.apwdevs.app.discover.databinding.FragmentMovieShowBinding
import id.apwdevs.app.discover.ui.DiscoverFragmentDirections
import id.apwdevs.app.discover.ui.FragmentMessenger
import id.apwdevs.app.res.adapter.ListMovieShowAdapter
import id.apwdevs.app.res.data.MovieShowItem
import id.apwdevs.app.res.util.PageType
import org.koin.android.viewmodel.ext.android.viewModel

class MovieShowFragment : Fragment() {

    private val movieShowViewModel: MovieShowViewModel by viewModel()
    private val movieShowAdapter: ListMovieShowAdapter by lazy { ListMovieShowAdapter(this::onItemClicked) }
    private val currentFragmentType: PageType by lazy { requireArguments().getParcelable(SHOWS_TYPE)!! }
    private val fragmentMessenger: FragmentMessenger by lazy { parentFragment as FragmentMessenger }

    private lateinit var binding: FragmentMovieShowBinding

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
        binding.recyclerview.adapter = movieShowAdapter
        movieShowViewModel.discoverPopular(currentFragmentType).observe(viewLifecycleOwner, this::observingData)

    }

    private fun observingData(data: PagingData<MovieShowItem>) {
        movieShowAdapter.submitData(lifecycle, data)
    }

    private fun onItemClicked(data: MovieShowItem) {
        fragmentMessenger.onItemClick(currentFragmentType, data)
    }

    companion object {

        const val SHOWS_TYPE: String = "SHOWS_FRAGMENT_TYPE"

        fun newInstance(type: PageType) : MovieShowFragment {
            return MovieShowFragment().apply {
                arguments = Bundle().also { it.putParcelable(SHOWS_TYPE, type)}
            }
        }

    }

}