package id.apwdevs.app.movieshow.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.apwdevs.app.movieshow.R
import id.apwdevs.app.movieshow.databinding.FragmentMainBinding
import id.apwdevs.app.movieshow.util.instantiateFeatureFragment

class MainFragment : Fragment() {

    private lateinit var fragmentMainBinding: FragmentMainBinding
    private lateinit var fragments: HashMap<Int, Fragment>
    private var currentFragmentKey: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false)
        return fragmentMainBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragments = instantiateFragments()
        attachFragmentsToFragmentManager()
        fragmentMainBinding.bottomNav.let {
            it.setOnNavigationItemSelectedListener(this::bottomNavImpl)
            it.selectedItemId = R.id.search_tab
        }
    }

    private fun instantiateFragments(): HashMap<Int, Fragment> {
        val qualifier = "id.apwdevs.app"
        return hashMapOf(
            R.id.search_tab  to instantiateFeatureFragment("$qualifier.search.ui.SearchFragment"),
            R.id.discover_tab to instantiateFeatureFragment("$qualifier.discover.ui.DiscoverFragment"),
            R.id.favorite_tab to instantiateFeatureFragment("$qualifier.favorite.ui.FavoriteFragment")
        )
    }

    private fun attachFragmentsToFragmentManager() {
        childFragmentManager.beginTransaction().apply {
            fragments.forEach { entry ->
                add(R.id.frame_container, entry.value)
                hide(entry.value)
            }
        }.commit()

    }

    private fun bottomNavImpl(item: MenuItem): Boolean {
        displayFragment(item.itemId)
        return true
    }

    private fun displayFragment(id: Int) {
        childFragmentManager.beginTransaction().apply {
            if(currentFragmentKey != 0)
                fragments[currentFragmentKey]?.let { hide(it) }

            currentFragmentKey = id
            fragments[currentFragmentKey]?.let { show(it) }
        }.commit()
    }


}