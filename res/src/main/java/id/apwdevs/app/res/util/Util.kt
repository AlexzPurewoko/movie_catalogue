package id.apwdevs.app.res.util

import android.view.View

val String?.backdropImageUrlPath : String?
    get() {
        return this?.let { "https://image.tmdb.org/t/p/w500${it}" }
    }

fun String?.getImageURL(size: String = "w500"): String? {
    return this?.let {"https://image.tmdb.org/t/p/$size${it}"}
}

//val Int?.isNullOrZero : Boolean
//        get() {
//            return when{
//                this == null -> true
//                this == 0 -> true
//                else -> false
//            }
//        }

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