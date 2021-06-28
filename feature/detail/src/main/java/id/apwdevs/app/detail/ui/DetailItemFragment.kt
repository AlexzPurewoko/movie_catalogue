package id.apwdevs.app.detail.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import id.apwdevs.app.detail.R
import id.apwdevs.app.detail.databinding.FragmentDetailBinding
import id.apwdevs.app.detail.di.detailModule
import id.apwdevs.app.detail.ui.helper.DetailItemHelper
import id.apwdevs.app.detail.ui.helper.DetailMovieHelper
import id.apwdevs.app.detail.ui.helper.DetailTvShowHelper
import id.apwdevs.app.detail.viewmodel.DetailMovieShowVM
import id.apwdevs.app.res.BaseFeatureFragment
import id.apwdevs.app.res.util.PageType
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.module.Module

class DetailItemFragment : BaseFeatureFragment() {

    private var args: DetailItemFragmentArgs? = null

    private val detailViewModel: DetailMovieShowVM by viewModel()

    private var detailHelper: DetailItemHelper? = null

    override val koinModules: List<Module>
        get() = listOf(detailModule)

    /*


        val viewContainer = FragmentDetailBinding.inflate(inflater, container, false)



        return viewContainer.root
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentDetailBinding.inflate(inflater, container, false).apply {
        args = DetailItemFragmentArgs.fromBundle(requireArguments())
        detailHelper = when (args?.pageType) {
            PageType.MOVIES -> {
                DetailMovieHelper(childFragmentManager, detailViewModel::loadData)
            }
            PageType.TV_SHOW -> {
                DetailTvShowHelper(childFragmentManager, detailViewModel::loadData)
            }
            else -> null
        }
        detailHelper?.onBindView(this)
        (activity as? AppCompatActivity)?.apply {
            setSupportActionBar(detailToolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        detailHelper?.init()

        detailViewModel.apply {
            pageType = args?.pageType
            itemId = args?.itemId ?: 0
            detailHelper?.let {
                data.observe(viewLifecycleOwner, it::bindObservedData)
                loadData()
                it.handleClickFavorite(::toggleFavorite)
            }

        }

    }

    override fun onDetach() {
        detailHelper?.destroy()
        detailHelper = null
        args = null
        (activity as? AppCompatActivity)?.setSupportActionBar(null)
        super.onDetach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_menu, menu)
        detailHelper?.favoriteMenuItem = menu.findItem(R.id.favorite_menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.favorite_menu) {
            detailViewModel.toggleFavorite()
            return true
        } else if (item.itemId == android.R.id.home) {
            findNavController().navigateUp()
        }
        return super.onOptionsItemSelected(item)
    }

}

