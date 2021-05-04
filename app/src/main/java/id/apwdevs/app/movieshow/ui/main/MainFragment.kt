package id.apwdevs.app.movieshow.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
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
        fragments = savedInstanceState?.let { loadFragmentState(it) } ?: instantiateFragments()

        attachFragmentsToFragmentManager()

        fragmentMainBinding.bottomNav.let {

            val bottomNavBg = it.background as MaterialShapeDrawable
            bottomNavBg.shapeAppearanceModel = bottomNavBg.shapeAppearanceModel.toBuilder().apply {
                setAllCorners(CornerFamily.ROUNDED, 40.0f)
            }.build()
            it.setOnNavigationItemSelectedListener(this::bottomNavImpl)
            it.selectedItemId = if (currentFragmentKey == 0) R.id.search_tab else currentFragmentKey
        }
    }

    private fun loadFragmentState(savedInstanceState: Bundle): HashMap<Int, Fragment> {
        currentFragmentKey = savedInstanceState.getInt(SAVED_FRAGMENT_KEY)

        val getFg: (Int) -> Fragment = { keyInt ->
            childFragmentManager.getFragment(savedInstanceState, keyInt.toString())
                ?: throw IllegalAccessException("Fragment key: $keyInt cannot be restored!")
        }
        return hashMapOf(
            R.id.search_tab to getFg(R.id.search_tab),
            R.id.discover_tab to getFg(R.id.discover_tab),
            R.id.favorite_tab to getFg(R.id.favorite_tab)
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        fragments.entries.forEach {
            childFragmentManager.putFragment(outState, it.key.toString(), it.value)
        }
        outState.putInt(SAVED_FRAGMENT_KEY, currentFragmentKey)
    }


    private fun instantiateFragments(): HashMap<Int, Fragment> {
        val qualifier = "id.apwdevs.app"
        return hashMapOf(
            R.id.search_tab to instantiateFeatureFragment("$qualifier.search.ui.SearchFragment"),
            R.id.discover_tab to instantiateFeatureFragment("$qualifier.discover.ui.DiscoverFragment"),
            R.id.favorite_tab to instantiateFeatureFragment("$qualifier.favorite.ui.FavoriteFragment")
        )
    }

    private fun attachFragmentsToFragmentManager() {

        if (childFragmentManager.fragments.isNotEmpty()) {
            childFragmentManager.commitNow {
                val list = childFragmentManager.fragments
                for (i in list) {
                    remove(i)
                }
            }

        }

        childFragmentManager.commit {

            fragments.forEach { entry ->
                add(R.id.frame_container, entry.value)
                hide(entry.value)
            }
        }


    }

    private fun bottomNavImpl(item: MenuItem): Boolean {
        displayFragment(item.itemId)
        return true
    }

    private fun displayFragment(id: Int) {
        childFragmentManager.beginTransaction().apply {
            if (currentFragmentKey != 0)
                fragments[currentFragmentKey]?.let { hide(it) }

            currentFragmentKey = id
            fragments[currentFragmentKey]?.let { show(it) }
        }.commit()
    }

    companion object {
        private const val SAVED_FRAGMENT_KEY = "saved_fragment_key"
    }

}