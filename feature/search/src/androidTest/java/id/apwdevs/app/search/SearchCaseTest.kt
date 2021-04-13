package id.apwdevs.app.search

import id.apwdevs.app.search.ui.SearchFragment
import id.apwdevs.app.test.androdtest.BaseAndroidTest
import id.apwdevs.app.test.androdtest.utils.mustBeDisplayed
import id.apwdevs.app.test.androdtest.utils.thisViewMustBeDisplayed
import id.apwdevs.app.test.androdtest.utils.viewMustBeHidden
import okhttp3.mockwebserver.Dispatcher
import org.junit.Test

class SearchCaseTest : BaseAndroidTest() {

    private lateinit var mockDispatcher: SearchMockDispatcher
    private lateinit var searchFragment: SearchFragment

    override fun setup() {
        super.setup()
        mockDispatcher = SearchMockDispatcher(context)
        mockWebServer.dispatcher = SearchMockDispatcher(context) as Dispatcher

        searchFragment = launchFragmentInContainer(SearchFragment::class.java)
    }

    @Test
    fun shouldDisplayMinimumConfig() {
        listOf(
            "button_search",
            "text_search",
            "spinner",
            "isAdult",
            "frame_status_container"
        ).forEach { it.thisViewMustBeDisplayed() }

        "recyclerView".viewMustBeHidden()
    }

    @Test
    fun shouldDisplay_empty_and_recommendation_at_first_moment() {
        "recyclerView".viewMustBeHidden()
        "frame_status_container".thisViewMustBeDisplayed()

        "lottie_anim".thisViewMustBeDisplayed()
        "Let's Search Whatever you Like".mustBeDisplayed()
    }

    @Test
    fun shouldDisplay_data_when_searching_success() {

    }

    @Test
    fun shouldDisplay_explanation_when_any_error_during_searching() {

    }

    @Test
    fun shouldTrigger_search_when_changing_filter_parameters() {

    }

    @Test
    fun shouldAble_to_display_detail_when_clicking_one_item() {

    }
}