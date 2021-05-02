package id.apwdevs.app.detail.ui.helper

import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import id.apwdevs.app.core.domain.model.Genre
import id.apwdevs.app.core.utils.State
import id.apwdevs.app.detail.databinding.FragmentDetailBinding
import id.apwdevs.app.detail.viewmodel.DetailMovieShowVM
import id.apwdevs.app.res.R
import id.apwdevs.app.res.util.gone
import id.apwdevs.app.res.util.visible
import java.util.*

abstract class DetailItemHelper(
    private val onRetry: () -> Unit
) {

    protected lateinit var rootBinding: FragmentDetailBinding
    var favoriteMenuItem: MenuItem? = null

    private val shimmerLoading: ShimmerFrameLayout by lazy {
        rootBinding.shimmerLoading
    }

    private val appBarLayoutOffsetChangeListener: AppBarLayout.OnOffsetChangedListener =
        AppBarLayout.OnOffsetChangedListener(::appBarOffsetChangeListener)


    abstract fun initView()
    protected abstract fun onSuccess(data: Any?)
    protected abstract fun onLoad()
    protected abstract fun onDestroy()

    protected fun composeGenre(chipGenre: ChipGroup, data: List<Genre>) {
        data.forEach { genre ->
            val chip = Chip(chipGenre.context).apply {
                layoutParams = ChipGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                this.isCheckable = false
                isChipIconVisible = false
                isCloseIconVisible = false
                text = genre.genreName
            }
            chipGenre.addView(chip)
        }
    }

    protected fun getLanguage(iso6391: String): CharSequence? {
        return Locale(iso6391).displayName
    }

    fun onBindView(bindingLayout: FragmentDetailBinding) {
        rootBinding = bindingLayout
        rootBinding.btnRetry.setOnClickListener { onRetry() }
        rootBinding.appBar.addOnOffsetChangedListener(appBarLayoutOffsetChangeListener)
    }

    fun bindObservedData(resData: State<DetailMovieShowVM.DataPostType>) {
        val widgetFav = rootBinding.favoriteFab
        when (resData) {
            is State.Error -> handleOnFailed()
            is State.Loading -> {
                widgetFav.isEnabled = false
                favoriteMenuItem?.isEnabled = false

                if(resData.loadingTag != DetailMovieShowVM.FAVORITE_LOADING_TAG){
                    onLoad()
                    loading(true)
                }
            }
            is State.Success -> {
                when(resData.data?.postType) {
                    DetailMovieShowVM.PostType.DATA -> {
                        errorState(false)
                        loading(false)
                        disableScrollState(false)
                        onSuccess(resData.data?.data)
                    }
                    DetailMovieShowVM.PostType.FAVORITE_STATE -> {
                        triggerFavorite(resData)
                    }
                }

            }
        }
    }

    private fun triggerFavorite(resFav: State<DetailMovieShowVM.DataPostType>) {
        val widgetFav = rootBinding.favoriteFab
        when (resFav) {
            is State.Error -> {
            }
            is State.Loading -> {
                widgetFav.isEnabled = false
                favoriteMenuItem?.isEnabled = false
            }
            is State.Success -> {
                val enabled = true
                val isFavorited = resFav.data?.data == true
                val img =
                    if (isFavorited) R.drawable.ic_baseline_favorite_24
                    else R.drawable.ic_baseline_favorite_border_24
                widgetFav.tag = if (isFavorited) "favorited" else "unfavorited"
                widgetFav.isEnabled = enabled
                favoriteMenuItem?.isEnabled = enabled
                widgetFav.setImageResource(img)
            }
        }
    }

    fun handleClickFavorite(caller: () -> Unit) {
        rootBinding.favoriteFab.setOnClickListener {
            caller()
        }
    }

    fun destroy() {
        loading(false)
        rootBinding.appBar.removeOnOffsetChangedListener(appBarLayoutOffsetChangeListener)
        onDestroy()
    }

    private fun handleOnFailed() {
        with(rootBinding) {
            favoriteFab.isEnabled = false
            disableScrollState(true)
            errorState(true)
            lottiePlaceholder.setAnimation("empty_box.json")
            posterImage.gone()
        }


    }


    private fun appBarOffsetChangeListener(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        val totalRange = appBarLayout?.totalScrollRange ?: return
        val currentPos = totalRange + verticalOffset

        // indicate the percent of scrolling
        // if 0, appBar is on the top until 100 is on bottom
        val scrollRate =
            if (currentPos == 0) 0
            else (currentPos * 100) / totalRange

        favoriteMenuItem?.isVisible = scrollRate < 20
//        if(scrollRate > 20)
    }

    private fun disableScrollState(disabled: Boolean) {
        with(rootBinding) {
            collapsingToolbar.layoutParams =
                AppBarLayout.LayoutParams(collapsingToolbar.layoutParams).apply {
                    scrollFlags =
                        if (disabled) AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL
                        else AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
                }
        }
    }

    private fun loading(state: Boolean) {
        if (!state) {
            shimmerLoading.stopShimmer()
            shimmerLoading.hideShimmer()
            shimmerLoading.gone()
            rootBinding.lottiePlaceholder.apply {
                pauseAnimation()
                gone()
            }
            rootBinding.posterImage.visible()
        } else {
            shimmerLoading.visible()
            shimmerLoading.showShimmer(true)
            rootBinding.lottiePlaceholder.apply {
                setAnimation("loading_scooter_riding.json")
                playAnimation()
                visible()
            }
            rootBinding.posterImage.gone()
        }
    }

    private fun errorState(displayed: Boolean) {
        rootBinding.errorDisplay.visibility = if (displayed) View.VISIBLE else View.GONE
    }
}