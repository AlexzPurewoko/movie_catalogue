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

    private lateinit var binding: FragmentFavoriteBinding

    @VisibleForTesting
    var currentPageView: PageType? = null

    private var tabLayoutMediator: TabLayoutMediator? = null

    private var onPageChangeCallback: ViewPager2.OnPageChangeCallback? = null

    override val koinModules: List<Module>
        get() = listOf(favoriteModule)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = FavoriteFragmentAdapter(requireActivity())

        tabLayoutMediator = TabLayoutMediator(
            binding.tabs, binding.viewPager, true
        ) { tab, position ->
            tab.text = getString(TAB_TITLES[position])

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
        onPageChangeCallback?.let { binding.viewPager.registerOnPageChangeCallback(it) }
    }

    override fun onDetach() {
        onPageChangeCallback?.let {
            binding.viewPager.unregisterOnPageChangeCallback(it)
        }
        onPageChangeCallback = null
        tabLayoutMediator?.detach()

        super.onDetach()
    }

    companion object {
        private val TAB_TITLES = listOf(
            R.string.tab_movies,
            R.string.tab_tvshows
        )
    }

}