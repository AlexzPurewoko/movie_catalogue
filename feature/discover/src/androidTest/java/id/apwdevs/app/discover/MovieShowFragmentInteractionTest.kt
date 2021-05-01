package id.apwdevs.app.discover

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import com.google.android.material.tabs.TabLayout
import id.apwdevs.app.discover.ui.DiscoverFragment
import id.apwdevs.app.res.util.PageType
import id.apwdevs.app.test.androdtest.BaseAndroidTest
import id.apwdevs.app.test.androdtest.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Test

class MovieShowFragmentInteractionTest: BaseAndroidTest() {

    private lateinit var fragment: DiscoverFragment

    override fun setup() {
        super.setup()
        fragment = launchFragmentInContainer(DiscoverFragment::class.java)
    }

    @Test
    fun should_display_minimal_ui() {
        runBlocking {
            "tabs".thisViewMustBeDisplayed()
            "pager_container".thisViewMustBeDisplayed()

            "Movie".toUpperCase().mustBeDisplayed()
            "Tv Show".toUpperCase().mustBeDisplayed()
        }

    }

    @Test
    fun should_display_correct_fragment_when_swiping(){
        swipeLeft()
        Assert.assertEquals(PageType.TV_SHOW, fragment.currentPageView)

        swipeRight()
        Assert.assertEquals(PageType.MOVIES, fragment.currentPageView)
    }

    @Test
    fun should_display_correct_fragment_when_clicking_tabs() {
        "tabs".performActionOnView(TabClickViewActionsAtPosition(1))
        Assert.assertEquals(PageType.TV_SHOW, fragment.currentPageView)

        "tabs".performActionOnView(TabClickViewActionsAtPosition(0))
        Assert.assertEquals(PageType.MOVIES, fragment.currentPageView)
    }
}

class TabClickViewActionsAtPosition(
    private val tabPosition: Int
) : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return Matchers.allOf(
            ViewMatchers.isAssignableFrom(TabLayout::class.java),
            ViewMatchers.isDisplayed()
        )
    }

    override fun getDescription(): String {
        return "Perform action on TabLayout"
    }

    override fun perform(uiController: UiController?, view: View?) {
        uiController?.loopMainThreadUntilIdle()

        val tabView = view as TabLayout
        val tabItem = tabView.getTabAt(tabPosition) as TabLayout.Tab

        tabItem.select()
        uiController?.loopMainThreadUntilIdle()
    }

}