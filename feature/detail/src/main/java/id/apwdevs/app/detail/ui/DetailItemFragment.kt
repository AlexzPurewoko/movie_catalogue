package id.apwdevs.app.detail.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
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

    private val args: DetailItemFragmentArgs by lazy {
        DetailItemFragmentArgs.fromBundle(requireArguments())
    }


    private val detailViewModel: DetailMovieShowVM by viewModel()
    lateinit var detailHelper: DetailItemHelper

    override val koinModules: List<Module>
        get() = listOf(detailModule)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewContainer = FragmentDetailBinding.inflate(inflater, container, false)
        detailHelper = when (args.pageType) {
            PageType.MOVIES -> {
                DetailMovieHelper(childFragmentManager, detailViewModel::loadData)
            }
            PageType.TV_SHOW -> {
                DetailTvShowHelper(childFragmentManager, detailViewModel::loadData)
            }
        }
        detailHelper.onBindView(viewContainer)
        (requireActivity() as AppCompatActivity).setSupportActionBar(viewContainer.detailToolbar)
        return viewContainer.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        detailHelper.initView()
        detailViewModel.pageType = args.pageType
        detailViewModel.itemId = args.itemId

        detailViewModel.data.observe(viewLifecycleOwner, detailHelper::bindObservedData)
        detailViewModel.loadData()
        detailHelper.handleClickFavorite(detailViewModel::toggleFavorite)

    }

    override fun onDetach() {
        detailHelper.destroy()
        super.onDetach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_menu, menu)
        detailHelper.favoriteMenuItem = menu.findItem(R.id.favorite_menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.favorite_menu) {
            detailViewModel.toggleFavorite()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}

