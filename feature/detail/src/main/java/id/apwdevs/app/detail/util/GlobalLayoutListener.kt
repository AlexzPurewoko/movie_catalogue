package id.apwdevs.app.detail.util

import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.marginTop
import com.google.android.material.chip.ChipGroup
import java.lang.ref.WeakReference

class GlobalLayoutListener(
    container: View,
    textTitleRefs: AppCompatTextView,
    ratingBarRefs: AppCompatRatingBar,
    chipGroupRefs: ChipGroup,
    callbackResult: (computeResult: Int, appBarComputeResult: Int, containerHeight: Int) -> Unit
) : ViewTreeObserver.OnGlobalLayoutListener {

    private val weakTextTitleRef = WeakReference(textTitleRefs)
    private val weakRatingBarRef = WeakReference(ratingBarRefs)
    private val weakChipGroupRef = WeakReference(chipGroupRefs)
    private val weakRootView = WeakReference(container)
    private val weakCallbackResult = WeakReference(callbackResult)

    private var iterations = 0

    override fun onGlobalLayout() {

        // To prevent infinite call loops from viewTreeObserver,
        // we need to define the max iterations of call
        if (iterations > MAX_ITERATIONS) return

        // getting all marginTop of view
        val marginTopTitle = marginTopOf(weakTextTitleRef)
        val marginTopRatingBar = marginTopOf(weakTextTitleRef)
        val marginTopChipGroup = marginTopOf(weakTextTitleRef)

        // sets the margin bottom

        // get the height of all elements
        val textTitleHeight = heightOf(weakTextTitleRef)
        val ratingBarHeight = heightOf(weakRatingBarRef)
        val chipGroupHeight = heightOf(weakChipGroupRef)

        // container sizes
        val containerHeight = heightOf(weakRootView)

        if (
            containerHeight != 0 &&
            marginTopTitle != 0 && marginTopRatingBar != 0 && marginTopChipGroup != 0 &&
            textTitleHeight != 0 && ratingBarHeight != 0 && chipGroupHeight != 0
        ) {
            val sumAllContentHeight = textTitleHeight + ratingBarHeight + chipGroupHeight
            val sumAllMarginTopContent = marginTopTitle + marginTopRatingBar + marginTopChipGroup
            val computeAll = (sumAllContentHeight + sumAllMarginTopContent + marginTopChipGroup)
            val result = containerHeight - computeAll

            weakCallbackResult.get()?.invoke(computeAll, result, containerHeight)
            iterations++
        }
    }

    private fun <T : View> marginTopOf(viewRef: WeakReference<T>): Int {
        return getVProps(viewRef) { it?.marginTop }
    }

    private fun <T : View> heightOf(viewRef: WeakReference<T>): Int {
        return getVProps(viewRef) { it?.measuredHeight }
    }

    private inline fun <T : View> getVProps(
        viewRef: WeakReference<T>,
        retValue: (View?) -> Int?
    ): Int {
        return retValue(viewRef.get()) ?: 0
    }

    @Suppress("unused")
    fun release() {
        weakChipGroupRef.clear()
        weakRatingBarRef.clear()
        weakTextTitleRef.clear()
        weakRootView.clear()
        weakCallbackResult.clear()
    }

    companion object {
        private const val MAX_ITERATIONS = 3
    }
}