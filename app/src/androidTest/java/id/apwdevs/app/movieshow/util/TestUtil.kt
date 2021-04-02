package id.apwdevs.app.movieshow.util

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.airbnb.lottie.LottieAnimationView
import id.apwdevs.app.movieshow.FragmentTestActivity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.Matchers.not

fun Context.provideResponse(customAssetJson: String, responseCode: Int) : MockResponse {

    return MockResponse()
        .setResponseCode(responseCode)
        .setBody(readJson(customAssetJson))
}

fun Context.readJson(path: String): String {
    val iStream = assets.open(path)
    val byteArr = ByteArray(iStream.available())
    iStream.read(byteArr)
    val strResult = String(byteArr)
    iStream.close()
    return strResult
}


/**
 * Provides a test on a resource name id view
 * That view must be displayed on a screen
 */
fun String.thisViewMustBeDisplayed() {
    onView(withResourceName(this)).check(matches(isDisplayed()))
}

/**
 * Provides a test with a Text
 * That text must be displayed on {@param resName}
 */
fun String.mustDisplayedOnView(resName: String){
    onView(withResourceName(resName)).check(matches(withText(this)))
}

fun String.thisViewMustContainTextExactly(str: String) {
    onView(withResourceName(this)).check(matches(withText(str)))
}

fun String.mustContainTagsExactly(tag: Any){
    onView(withResourceName(this)).check(ViewTagAssertion(tag))
}

fun String.clickThis() {
    onView(withResourceName(this)).perform(click())
}

fun String.mustBeDisplayed() {
    onView(withText(this)).check(matches(isDisplayed()))
}

fun String.viewDoesNotExists() {
    onView(withResourceName(this)).check(doesNotExist())
}

fun String.viewMustBeHidden() {
    onView(withResourceName(this)).check(matches(not(isDisplayed())))
}



fun swipeUp() {
    onView(isRoot()).perform(swipeUp())
}


class ViewTagAssertion(
    private val tagValue: Any
): ViewAssertion {
    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        noViewFoundException?.let { throw noViewFoundException }

        assertNotNull(view)
        assertEquals(view!!.tag, tagValue)
    }
}

class ViewAssertionRunner(
    private val runner: (View?) -> Unit
): ViewAssertion {
    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        noViewFoundException?.let { throw noViewFoundException }
        runner(view)
    }
}