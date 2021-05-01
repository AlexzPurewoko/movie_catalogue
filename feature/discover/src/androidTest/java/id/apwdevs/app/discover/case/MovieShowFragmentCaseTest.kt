package id.apwdevs.app.discover.case

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withResourceName
import id.apwdevs.app.discover.di.discoverViewModel
import id.apwdevs.app.discover.ui.child.MovieShowFragment
import id.apwdevs.app.res.util.PageType
import id.apwdevs.app.test.androdtest.BaseAndroidTest
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertNull
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

abstract class MovieShowFragmentCaseTest : BaseAndroidTest() {

    protected abstract val pageType: PageType


    override fun setup() {
        super.setup()
        loadKoinModules(discoverViewModel)
    }

    override fun tearDown() {
        super.tearDown()
        unloadKoinModules(discoverViewModel)
    }

    @Test
    open fun should_display_minimal_ui() {
        launchFragment()

        onView(withResourceName("recyclerView")).check { view, noViewFoundException ->
            assertNotNull(view)
            assertNull(noViewFoundException)
        }

        onView(withResourceName("frameStatus")).check { view, noViewFoundException ->
            assertNotNull(view)
            assertNull(noViewFoundException)
        }
    }

    @Test
    abstract fun should_display_data_when_connected_to_internet()

    @Test
    abstract fun should_display_empty_data_when_receive_empty_data()

    @Test
    abstract fun should_display_error_when_failed_get_data()

    @Test
    abstract fun should_be_able_to_refresh_data_after_clicking_try_again()

    protected fun launchFragment() =
        launchFragmentInContainer(MovieShowFragment.newInstance(pageType))
}