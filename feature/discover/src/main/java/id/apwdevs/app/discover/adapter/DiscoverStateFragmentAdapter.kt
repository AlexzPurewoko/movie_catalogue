package id.apwdevs.app.discover.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.apwdevs.app.discover.ui.child.MovieShowFragment
import id.apwdevs.app.res.util.PageType

class DiscoverStateFragmentAdapter(fm: Fragment, onclick: () -> Unit) : FragmentStateAdapter(fm) {
    private val listFragment = listOf(
        MovieShowFragment.newInstance(PageType.MOVIES),
        MovieShowFragment.newInstance(PageType.TV_SHOW)
    )

    override fun getItemCount(): Int = listFragment.size

    override fun createFragment(position: Int): Fragment {
        return listFragment[position]
    }
}