package id.apwdevs.app.discover.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.apwdevs.app.discover.ui.child.MovieShowFragment
import id.apwdevs.app.res.util.PageType
import java.lang.IllegalStateException

class DiscoverStateFragmentAdapter(fm: Fragment) : FragmentStateAdapter(fm) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MovieShowFragment.newInstance(PageType.MOVIES)
            1 -> MovieShowFragment.newInstance(PageType.TV_SHOW)
            else -> throw IllegalStateException("Invalid Fragment!")
        }
    }
}