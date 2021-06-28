package id.apwdevs.app.movieshow

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import id.apwdevs.app.detail.ui.DetailItemFragmentArgs
import id.apwdevs.app.movieshow.util.instantiateFeatureFragment
import id.apwdevs.app.res.util.PageType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FragmentTestActivity : AppCompatActivity() {

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

//        Bundle().also {
//            it.putString(STRING_DATA, data)
//            it.putString(TITLE_STRING, titleMovie)
//        }
//        val method = Class.forName("id.apwdevs.app.detail.ui.component.CardBackdropItem").getMethod("newInstance", String::class.java, String::class.java)
//        var fg = method.invoke(null, "https://image.tmdb.org/t/p/w500/6MKr3KgOLmzOP6MSuZERO41Lpkt.jpg", "Cruella") as? Fragment
        setFragment(instantiateFeatureFragment("id.apwdevs.app.detail.ui.DetailItemFragment").apply {
            arguments = DetailItemFragmentArgs(PageType.TV_SHOW, 88396).toBundle()
        })
//        GlobalScope.launch(Dispatchers.Main) {
//            delay(2000)
//            supportFragmentManager.commitNow {
//                remove(fg!!)
//            }
//            delay(5000)
//            fg = null
//            delay(1000)
//            val fsg = method.invoke(null, "https://image.tmdb.org/t/p/w500/6MKr3KgOLmzOP6MSuZERO41Lpkt.jpg", "Cruella") as Fragment
//            setFragment(fsg)
//        }
    }

    fun setFragment(fragment: Fragment?) {
        if(fragment == null) return
        supportFragmentManager.commitNow {
            replace(R.id.frame_container, fragment)
        }
    }

}