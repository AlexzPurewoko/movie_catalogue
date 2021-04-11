package id.apwdevs.app.discover.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import id.apwdevs.app.discover.adapter.DiscoverStateFragmentAdapter
import id.apwdevs.app.discover.databinding.FragmentDiscoverBinding
import id.apwdevs.app.discover.di.discoverViewModel
import id.apwdevs.app.movieshow.R
import id.apwdevs.app.movieshow.ui.main.MainFragmentDirections
import id.apwdevs.app.res.BaseFeatureFragment
import id.apwdevs.app.res.data.MovieShowItem
import id.apwdevs.app.res.util.PageType
import org.koin.core.module.Module

class DiscoverFragment : BaseFeatureFragment(), FragmentMessenger {

    private lateinit var binding: FragmentDiscoverBinding

    private val viewPagerAdapter: DiscoverStateFragmentAdapter by lazy {
        DiscoverStateFragmentAdapter(this, ::onclick)
    }

    private fun onclick() {
        TODO("Not yet implemented")
    }

    override val koinModules: List<Module>
        get() = listOf(discoverViewModel)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeTab(binding.tabs, binding.pagerContainer)
    }

    override fun onItemClick(pageType: PageType, item: Any) {

        val directions = MainFragmentDirections.actionMainFragmentToDetailFragment(
            PageType.MOVIES,
            (item as MovieShowItem).id
        )
        findNavController().navigate(directions)
    }

    private fun initializeTab(tabs: TabLayout, pagerContainer: ViewPager2) {
        pagerContainer.adapter = viewPagerAdapter
        TabLayoutMediator(tabs, pagerContainer) { tab, position ->
            tab.text = getString(TABS[position])
        }.attach()
    }


    companion object {
        private val TABS = intArrayOf(
            R.string.movies,
            R.string.tvshows
        )
    }
}