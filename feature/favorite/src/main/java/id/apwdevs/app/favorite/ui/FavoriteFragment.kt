package id.apwdevs.app.favorite.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import id.apwdevs.app.favorite.R
import id.apwdevs.app.favorite.adapter.FavoriteFragmentAdapter
import id.apwdevs.app.favorite.databinding.FragmentFavoriteBinding
import id.apwdevs.app.favorite.di.favoriteModule
import id.apwdevs.app.res.BaseFeatureFragment
import id.apwdevs.app.res.util.OnPageSelectedChangeCallback
import id.apwdevs.app.res.util.PageType
import org.koin.core.module.Module

class FavoriteFragment : BaseFeatureFragment() {

    private var binding: FragmentFavoriteBinding? = null

    @VisibleForTesting
    var currentPageView: PageType? = null

    private var tabLayoutMediator: TabLayoutMediator? = null

    private var onPageChangeCallback: ViewPager2.OnPageChangeCallback? = null

    override val koinModules: List<Module>
        get() = listOf(favoriteModule)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding?.let {
            it.viewPager.adapter = FavoriteFragmentAdapter(this)
            tabLayoutMediator = TabLayoutMediator(
                it.tabs, it.viewPager, true
            ) { tab, position ->
                tab.text = getString(TAB_TITLES[position])

            }
        }
        tabLayoutMediator?.attach()
        registerPageChangeCallback()

    }

    private fun registerPageChangeCallback() {
        onPageChangeCallback = OnPageSelectedChangeCallback {
            currentPageView = when (it) {
                0 -> PageType.MOVIES
                1 -> PageType.TV_SHOW
                else -> null
            }
        }
        onPageChangeCallback?.let { 
            binding?.viewPager?.registerOnPageChangeCallback(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onPageChangeCallback?.let {
            binding?.viewPager?.unregisterOnPageChangeCallback(it)
        }
        onPageChangeCallback = null
        tabLayoutMediator?.detach()
        tabLayoutMediator = null

        binding = null
    }

    companion object {
        private val TAB_TITLES = listOf(
            R.string.tab_movies,
            R.string.tab_tvshows
        )
    }

}