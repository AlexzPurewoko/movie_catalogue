package id.apwdevs.app.discover.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import id.apwdevs.app.discover.adapter.DiscoverStateFragmentAdapter
import id.apwdevs.app.discover.databinding.FragmentDiscoverBinding
import id.apwdevs.app.discover.di.discoverViewModel
import id.apwdevs.app.movieshow.R
import id.apwdevs.app.res.BaseFeatureFragment
import id.apwdevs.app.res.util.OnPageSelectedChangeCallback
import id.apwdevs.app.res.util.PageType
import org.koin.core.module.Module
import java.lang.ref.WeakReference

class DiscoverFragment : BaseFeatureFragment() {

    private var binding: FragmentDiscoverBinding? = null

    @VisibleForTesting
    var currentPageView: PageType? = null

    private var tabLayoutMediator: SafeTabLayoutMediator? = null

    private var onPageChangeCallback: ViewPager2.OnPageChangeCallback? = null

    override val koinModules: List<Module>
        get() = listOf(discoverViewModel)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeTab(binding?.tabs, binding?.pagerContainer)
    }

    private fun initializeTab(tabs: TabLayout?, pagerContainer: ViewPager2?) {

        pagerContainer?.adapter = DiscoverStateFragmentAdapter(this)
        tabLayoutMediator = tabs?.let { tab ->
            pagerContainer?.let {
                SafeTabLayoutMediator(tab, it) { tab, position ->
                    tab?.text = getString(TABS[position])
                }
            }
        }

        onPageChangeCallback = OnPageSelectedChangeCallback {
            currentPageView = when (it) {
                0 -> PageType.MOVIES
                1 -> PageType.TV_SHOW
                else -> null
            }
        }
        onPageChangeCallback?.let { pagerContainer?.registerOnPageChangeCallback(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onPageChangeCallback?.let {
            binding?.pagerContainer?.unregisterOnPageChangeCallback(it)
        }
        tabLayoutMediator?.detach()
        onPageChangeCallback = null
        tabLayoutMediator = null
        binding = null
    }

    companion object {
        private val TABS = intArrayOf(
            R.string.movies,
            R.string.tvshows
        )
    }
}

class SafeTabLayoutMediator(
    tab: TabLayout,
    viewPager2: ViewPager2,
    private val initImpl: (tab: TabLayout.Tab?, position: Int) -> Unit) {

    private val tabViews = WeakReference(tab)
    private val viewPager = WeakReference(viewPager2)

    private val tabListener: WeakReference<TabLayout.OnTabSelectedListener> = composeTabListener()

    private fun composeTabListener(): WeakReference<TabLayout.OnTabSelectedListener> {
        val onTabChangeListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.get()?.setCurrentItem(tab?.position ?: 0, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        }
        return WeakReference(onTabChangeListener)
    }

    private val viewPagerListener: WeakReference<ViewPager2.OnPageChangeCallback> = composeVPagerListener()

    private fun composeVPagerListener(): WeakReference<ViewPager2.OnPageChangeCallback> {
        val onPageChange = object : ViewPager2.OnPageChangeCallback() {
            private var prevScrollState: Int = ViewPager2.SCROLL_STATE_IDLE
            private var scrollState: Int = ViewPager2.SCROLL_STATE_IDLE

            override fun onPageScrollStateChanged(state: Int) {
                prevScrollState = scrollState
                scrollState = state
            }

            override fun onPageSelected(position: Int) {
                tabViews.get()?.apply {
                    if (selectedTabPosition != position && position < tabCount) {
                        // Select the tab, only updating the indicator if we're not being dragged/settled
                        // (since onPageScrolled will handle that).
                        val updateIndicator = (scrollState == ViewPager2.SCROLL_STATE_IDLE
                                || (scrollState == ViewPager2.SCROLL_STATE_SETTLING
                                && prevScrollState == ViewPager2.SCROLL_STATE_IDLE))
                        selectTab(getTabAt(position), updateIndicator)
                    }
                }
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                tabViews.get()?.apply {
                    val updateText = scrollState != ViewPager2.SCROLL_STATE_SETTLING || prevScrollState == ViewPager2.SCROLL_STATE_DRAGGING
                    val updateIndicator =
                        !(scrollState == ViewPager2.SCROLL_STATE_SETTLING && prevScrollState == ViewPager2.SCROLL_STATE_IDLE)
                    tabViews.get()?.setScrollPosition(position, positionOffset, updateText, updateIndicator)
                }
            }
        }
        return WeakReference(onPageChange)
    }

    init {
        viewPager.get()?.adapter?.itemCount?.let { treshold ->
            for (i in 0 until treshold) {
                val tabItem = tabViews.get()!!.newTab()
                tabViews.get()!!.addTab(tabItem)
                initImpl(tabItem, i)
            }
        }

        tabViews.get()?.addOnTabSelectedListener(tabListener.get()!!)
        viewPager.get()?.registerOnPageChangeCallback(viewPagerListener.get()!!)
        tabViews.get()?.setScrollPosition(viewPager.get()!!.currentItem, 0f, true)
    }

    fun detach() {

        tabListener.get()?.let {
            tabViews.get()?.removeOnTabSelectedListener(it)
        }

        viewPagerListener.get()?.let {
            viewPager.get()?.unregisterOnPageChangeCallback(it)
        }
        tabListener.clear()
        viewPagerListener.clear()
        tabViews.clear()
        viewPager.clear()
    }
}