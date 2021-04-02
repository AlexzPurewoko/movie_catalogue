package id.apwdevs.app.movieshow.util

import androidx.fragment.app.Fragment

fun instantiateFeatureFragment(classpath: String): Fragment {
    return Class.forName(classpath).newInstance() as Fragment
}