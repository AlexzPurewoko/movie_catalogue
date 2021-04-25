package id.apwdevs.app.favorite

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import id.apwdevs.app.favorite.testcase.FavoriteFragmentInteractionCaseTest
import id.apwdevs.app.favorite.ui.FavoriteFragment
import id.apwdevs.app.res.util.PageType
import id.apwdevs.app.test.androdtest.utils.performActionOnView
import id.apwdevs.app.test.androdtest.utils.swipeLeft
import id.apwdevs.app.test.androdtest.utils.swipeRight
import id.apwdevs.app.test.androdtest.utils.swipeUp
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.Assert

class FavoriteFragmentTest: FavoriteFragmentInteractionCaseTest() {

    private lateinit var fragment: FavoriteFragment
    override fun setup() {
        super.setup()
        fragment = launchFragmentInContainer(FavoriteFragment::class.java)
    }
    override fun should_display_correct_fragment_when_swiping() {
        swipeRight()
        Assert.assertEquals(PageType.TV_SHOW, fragment.currentPageView)

        swipeUp()
        swipeLeft()
        Assert.assertEquals(PageType.MOVIES, fragment.currentPageView)
    }

    override fun should_display_correct_fragment_when_clicking_tabs() {
        "tabs".performActionOnView(TabClickViewActionsAtPosition(1))
        Assert.assertEquals(PageType.TV_SHOW, fragment.currentPageView)

        "tabs".performActionOnView(TabClickViewActionsAtPosition(0))
        Assert.assertEquals(PageType.MOVIES, fragment.currentPageView)
    }
}

class TabClickViewActionsAtPosition(
    private val tabPosition: Int
): ViewAction {
    override fun getConstraints(): Matcher<View> {
        return Matchers.allOf(
            ViewMatchers.isAssignableFrom(TabLayout::class.java),
            ViewMatchers.isDisplayed())
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