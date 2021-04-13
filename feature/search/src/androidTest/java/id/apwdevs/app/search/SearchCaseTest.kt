package id.apwdevs.app.search

import id.apwdevs.app.test.androdtest.BaseAndroidTest
import org.junit.Test

class SearchCaseTest : BaseAndroidTest() {

    private lateinit var mockDispatcher: SearchMockDispatcher

    override fun setup() {
        super.setup()
        mockDispatcher = SearchMockDispatcher(context)
        mockWebServer.dispatcher = mockDispatcher
    }

    @Test
    fun shouldDisplay_empty_and_recommendation_at_first_moment() {

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