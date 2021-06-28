package id.apwdevs.app.discover.adapter

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.apwdevs.app.discover.ui.child.MovieShowFragment
import id.apwdevs.app.res.util.PageType

class DiscoverStateFragmentAdapter(fm: Fragment) : FragmentStateAdapter(fm.childFragmentManager, fm.viewLifecycleOwner.lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MovieShowFragment.newInstance(PageType.MOVIES)
            1 -> MovieShowFragment.newInstance(PageType.TV_SHOW)
            else -> throw IllegalStateException("Invalid Fragment!")
        }
    }
}