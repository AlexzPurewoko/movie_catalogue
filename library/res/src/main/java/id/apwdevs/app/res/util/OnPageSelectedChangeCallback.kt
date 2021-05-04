package id.apwdevs.app.res.util

import androidx.viewpager2.widget.ViewPager2
import java.lang.ref.WeakReference

typealias SelectedFunc = (Int) -> Unit

class OnPageSelectedChangeCallback(
    onSelected: SelectedFunc
) : ViewPager2.OnPageChangeCallback() {

    private val weakReference: WeakReference<SelectedFunc> = WeakReference(onSelected)

    override fun onPageSelected(position: Int) {
        weakReference.get()?.invoke(position)
    }

}