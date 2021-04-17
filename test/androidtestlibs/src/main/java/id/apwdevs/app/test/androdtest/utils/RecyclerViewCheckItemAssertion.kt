package id.apwdevs.app.test.androdtest.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import org.junit.Assert

class RecyclerViewCheckItemAssertion(private val callbackChecker: (RecyclerView) -> Unit) :
    ViewAssertion {
    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        noViewFoundException?.let { throw noViewFoundException }

        Assert.assertNotNull("Reference of view must not be null", view)
        Assert.assertTrue("Reference View must be a Recyclerview!", view is RecyclerView)

        (view as? RecyclerView)?.apply {
            callbackChecker.invoke(this)
        }
    }

}