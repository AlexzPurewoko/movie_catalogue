package id.apwdevs.app.movieshow

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commitNow

class FragmentTestActivity: AppCompatActivity() {

    private val frameLayout: FrameLayout by lazy {
        FrameLayout(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            id = R.id.frame_container
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(frameLayout)
    }

    fun setFragment(fragment: Fragment) {
        supportFragmentManager.commitNow {
            replace(R.id.frame_container, fragment)
        }
    }

}