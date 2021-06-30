package id.apwdevs.app.detail.ui.helper

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.MenuItem
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import id.apwdevs.app.core.domain.model.Genre
import id.apwdevs.app.core.utils.State
import id.apwdevs.app.detail.databinding.FragmentDetailBinding
import id.apwdevs.app.detail.util.GlobalLayoutListener
import id.apwdevs.app.detail.viewmodel.DetailMovieShowVM
import id.apwdevs.app.res.R
import id.apwdevs.app.res.util.changeStateDisplay
import id.apwdevs.app.res.util.gone
import id.apwdevs.app.res.util.visible
import java.util.*

abstract class DetailItemHelper(
    private val onRetry: () -> Unit
) {

    protected var rootBinding: FragmentDetailBinding? = null
    var favoriteMenuItem: MenuItem? = null

    private var shimmerLoading: ShimmerFrameLayout? = null

    private var appBarLayoutOffsetChangeListener: AppBarLayout.OnOffsetChangedListener? = null

    private var globalLayoutListener: GlobalLayoutListener? = null


    protected abstract fun initView()

    protected abstract fun onSuccess(data: Any?)

    protected abstract fun onLoad()

    protected abstract fun provideGlobalLayoutListener(callback: (Int, Int, Int) -> Unit): GlobalLayoutListener?

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

    fun onBindView(bindingLayout: FragmentDetailBinding?) {
        rootBinding = bindingLayout
        shimmerLoading = rootBinding?.shimmerLoading
        rootBinding?.apply {
            btnRetry.setOnClickListener { onRetry() }
            appBarLayoutOffsetChangeListener = AppBarLayout.OnOffsetChangedListener(this@DetailItemHelper::appBarOffsetChangeListener)
            appBar.addOnOffsetChangedListener(appBarLayoutOffsetChangeListener)
            posterImage.imageTintMode = PorterDuff.Mode.OVERLAY
            posterImage.imageTintList = ColorStateList.valueOf(BASE_COLOR_TOOLBAR)
            collapsingToolbar.setContentScrimColor(BASE_COLOR_TOOLBAR)
            collapsingToolbar.setStatusBarScrimColor(BASE_COLOR_TOOLBAR)
        }

    }

    fun bindObservedData(resData: State<DetailMovieShowVM.DataPostType>) {
        when (resData) {
            is State.Error -> handleOnFailed()
            is State.Loading -> {
                rootBinding?.favoriteFab?.isEnabled = false
                favoriteMenuItem?.isEnabled = false

                if (resData.loadingTag != DetailMovieShowVM.FAVORITE_LOADING_TAG) {
                    onLoad()
                    loading(true)
                }
            }
            is State.Success -> {
                when (resData.data?.postType) {
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
        rootBinding?.favoriteFab?.let { widgetFav ->
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

    }

    fun handleClickFavorite(caller: () -> Unit) {
        rootBinding?.favoriteFab?.setOnClickListener {
            caller()
        }
    }

    fun init() {
        initView()
        globalLayoutListener = provideGlobalLayoutListener(::onReceiveFromLayoutObserver)
        rootBinding?.root?.viewTreeObserver?.addOnGlobalLayoutListener(globalLayoutListener)
    }

    fun destroy() {
        loading(false)
        rootBinding?.appBar?.removeOnOffsetChangedListener(appBarLayoutOffsetChangeListener)
        appBarLayoutOffsetChangeListener = null
        removeGlobalLayoutListener()

        onDestroy()
    }

    private fun onReceiveFromLayoutObserver(
        computeResult: Int,
        appBarComputeResult: Int,
        containerHeight: Int
    ) {
        rootBinding?.appBar?.layoutParams = rootBinding?.appBar?.layoutParams?.apply {
            height = appBarComputeResult
        }
    }

    private fun removeGlobalLayoutListener() {
        globalLayoutListener?.apply {
            rootBinding?.root?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
            release()
        }
        globalLayoutListener = null
    }

    private fun handleOnFailed() {
        rootBinding?.apply {
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

        rootBinding?.nestedScroll?.layoutParams =
            (rootBinding?.nestedScroll?.layoutParams as? CoordinatorLayout.LayoutParams)?.apply {
                this.topMargin = if (scrollRate < 15) 10 else -8
            }

    }

    private fun disableScrollState(disabled: Boolean) {
        rootBinding?.apply {
            collapsingToolbar.layoutParams =
                AppBarLayout.LayoutParams(collapsingToolbar.layoutParams).apply {
                    scrollFlags =
                        if (disabled) AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL
                        else AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
                }
        }
    }

    private fun loading(state: Boolean) {
        shimmerLoading?.let {
            if (!state) {
                it.stopShimmer()
                it.hideShimmer()
                it.gone()
                rootBinding?.lottiePlaceholder?.apply {
                    pauseAnimation()
                    gone()
                }
                rootBinding?.posterImage?.visible()
            } else {
                it.visible()
                it.showShimmer(true)
                rootBinding?.lottiePlaceholder?.apply {
                    setAnimation("loading_scooter_riding.json")
                    playAnimation()
                    visible()
                }
                rootBinding?.posterImage?.gone()
            }
        }
    }

    private fun errorState(displayed: Boolean) {
        rootBinding?.errorDisplay?.changeStateDisplay(displayed)
    }

    companion object {
        private val BASE_COLOR_TOOLBAR = Color.parseColor("#202020")
    }
}