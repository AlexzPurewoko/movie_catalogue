package id.apwdevs.app.favorite.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.apwdevs.app.favorite.ui.content.FragmentContent
import id.apwdevs.app.res.util.PageType

class FavoriteFragmentAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment.childFragmentManager, fragment.viewLifecycleOwner.lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> FragmentContent.newInstance(PageType.MOVIES)
            1 -> FragmentContent.newInstance(PageType.TV_SHOW)
            else -> throw IndexOutOfBoundsException("Index must be not equal with $itemCount")
        }

}