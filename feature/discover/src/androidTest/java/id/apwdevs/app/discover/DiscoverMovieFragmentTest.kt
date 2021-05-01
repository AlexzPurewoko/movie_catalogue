package id.apwdevs.app.discover

import id.apwdevs.app.discover.case.MovieShowFragmentCaseTest
import id.apwdevs.app.discover.dispatcher.DiscoverMockDispatcher
import id.apwdevs.app.discover.dispatcher.EmptyMockDispatcher
import id.apwdevs.app.res.util.PageType
import id.apwdevs.app.test.androdtest.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.QueueDispatcher
import org.junit.Assert

class DiscoverMovieFragmentTest: MovieShowFragmentCaseTest() {

    override val pageType: PageType
        get() = PageType.MOVIES

    override fun should_display_data_when_connected_to_internet() {
        runBlocking {
            mockWebServer.dispatcher = DiscoverMockDispatcher(context)
            launchFragment()

            delay(1000)
            "frameStatus".viewMustBeHidden()
            "recyclerView".thisViewMustBeDisplayed()
            "recyclerView".performCheckOnView(
                RecyclerViewCheckItemAssertion {
                    val displayedItems = it.childCount
                    Assert.assertTrue("Items must be displayed on the view", displayedItems > 0)
                }
            )
        }
    }

    override fun should_display_empty_data_when_receive_empty_data() {
        runBlocking {
            mockWebServer.dispatcher = EmptyMockDispatcher(context)
            launchFragment()

            delay(1000)
            "recyclerView".viewMustBeHidden()
            "frameStatus".thisViewMustBeDisplayed()

            "Empty Data".mustBeDisplayed()
        }


    }

    override fun should_display_error_when_failed_get_data() {
        runBlocking {
            mockWebServer.dispatcher = QueueDispatcher()
            mockWebServer.enqueue(MockResponse().setResponseCode(404))
            launchFragment()

            delay(1000)
            "recyclerView".viewMustBeHidden()
            "frameStatus".thisViewMustBeDisplayed()

            "Error while retrieve data".mustBeDisplayed()
            "Retry".mustBeDisplayed()
        }
    }

    override fun should_be_able_to_refresh_data_after_clicking_try_again() {
        runBlocking {
            mockWebServer.dispatcher = QueueDispatcher()
            mockWebServer.enqueue(MockResponse().setResponseCode(404))
            launchFragment()

            delay(1000)
            "recyclerView".viewMustBeHidden()
            "frameStatus".thisViewMustBeDisplayed()

            "Retry".mustBeDisplayed()
            mockWebServer.dispatcher = DiscoverMockDispatcher(context)

            "Retry".clickThisText()

            delay(1000)
            "frameStatus".viewMustBeHidden()
            "recyclerView".thisViewMustBeDisplayed()
            "recyclerView".performCheckOnView(
                RecyclerViewCheckItemAssertion {
                    val displayedItems = it.childCount
                    Assert.assertTrue("Items must be displayed on the view", displayedItems > 0)
                }
            )
        }

    }

}