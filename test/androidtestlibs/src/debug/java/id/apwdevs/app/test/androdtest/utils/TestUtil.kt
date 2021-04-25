package id.apwdevs.app.test.androdtest.utils

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matchers
import org.junit.Assert


/**
 * Provides a test on a resource name id view
 * That view must be displayed on a screen
 */
fun String.thisViewMustBeDisplayed() {
    Espresso.onView(ViewMatchers.withResourceName(this))
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
}

/**
 * Provides a test with a Text
 * That text must be displayed on {@param resName}
 */
fun String.mustDisplayedOnView(resName: String) {
    Espresso.onView(ViewMatchers.withResourceName(resName))
        .check(ViewAssertions.matches(ViewMatchers.withText(this)))
}

fun String.thisViewMustContainTextExactly(str: String) {
    Espresso.onView(ViewMatchers.withResourceName(this))
        .check(ViewAssertions.matches(ViewMatchers.withText(str)))
}

fun String.mustContainTagsExactly(tag: Any) {
    Espresso.onView(ViewMatchers.withResourceName(this)).check(ViewTagAssertion(tag))
}

fun String.fillViewWithText(s: String) {
    Espresso.onView(ViewMatchers.withResourceName(this)).perform(typeText(s))
}

fun String.performCheckOnView(viewAssertion: ViewAssertion) {
    Espresso.onView(ViewMatchers.withResourceName(this)).check(viewAssertion)
}

fun String.performActionOnView(vararg viewActions: ViewAction) {
    Espresso.onView(ViewMatchers.withResourceName(this)).perform(*viewActions)
}

fun String.clickThis() {
    Espresso.onView(ViewMatchers.withResourceName(this)).perform(ViewActions.click())
}

fun String.clickThisText() {
    Espresso.onView(ViewMatchers.withText(this)).perform(ViewActions.click())
}

fun String.mustBeDisplayed() {
    Espresso.onView(ViewMatchers.withText(this))
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
}

fun String.viewDoesNotExists() {
    Espresso.onView(ViewMatchers.withResourceName(this)).check(ViewAssertions.doesNotExist())
}

fun String.viewMustBeHidden() {
    Espresso.onView(ViewMatchers.withResourceName(this))
        .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
}


fun swipeUp() {
    Espresso.onView(ViewMatchers.isRoot()).perform(ViewActions.swipeUp())
}

fun swipeLeft() {
    Espresso.onView(ViewMatchers.isRoot()).perform(ViewActions.swipeUp())
}

fun swipeRight() {
    Espresso.onView(ViewMatchers.isRoot()).perform(ViewActions.swipeUp())
}


class ViewTagAssertion(
    private val tagValue: Any
) : ViewAssertion {
    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        noViewFoundException?.let { throw noViewFoundException }

        Assert.assertNotNull(view)
        Assert.assertEquals(view!!.tag, tagValue)
    }
}

class ViewAssertionRunner(
    private val runner: (View?) -> Unit
) : ViewAssertion {
    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        noViewFoundException?.let { throw noViewFoundException }
        runner(view)
    }
}