package id.apwdevs.app.res.util

import android.view.View

val String?.backdropImageUrlPath : String?
    get() {
        return this?.let { "https://image.tmdb.org/t/p/w500${it}" }
    }

fun String?.getImageURL(size: String = "w500"): String? {
    return this?.let {"https://image.tmdb.org/t/p/$size${it}"}
}

val Int?.zeroIfNull : Int
get() {
    return this ?: 0
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.changeStateDisplay(isDisplayed: Boolean){
    visibility = if(isDisplayed) View.GONE else View.GONE
}

fun Float.convertRatingFrom10to5() =
    if(this == 0.0f) 0.0f else this / 10 * 5