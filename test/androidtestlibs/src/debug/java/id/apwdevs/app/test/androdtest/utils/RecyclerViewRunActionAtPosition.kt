package id.apwdevs.app.test.androdtest.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher
import org.hamcrest.Matchers

class RecyclerViewRunActionAtPosition<T: RecyclerView.ViewHolder>(
    private val position: Int,
    private val selectedViewToPerform: (T) -> View,
    private val viewAction: ViewAction
) : ViewAction {

    override fun getConstraints(): Matcher<View> =
        Matchers.allOf(ViewMatchers.isAssignableFrom(RecyclerView::class.java),
            ViewMatchers.isDisplayed())

    override fun getDescription(): String =
        "Performing ViewActions on position: $position"

    override fun perform(uiController: UiController?, view: View?) {

        RecyclerViewActions.scrollToPosition<T>(position).perform(uiController, view)
        uiController?.loopMainThreadUntilIdle()

        val recyclerView = view as RecyclerView
        val s = recyclerView.findViewHolderForAdapterPosition(position)
        val resultView = selectedViewToPerform.invoke(s as T)

        viewAction.perform(uiController, resultView)
    }

}