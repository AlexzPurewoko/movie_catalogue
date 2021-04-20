package id.apwdevs.app.search

import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.action.ViewActions.click
import id.apwdevs.app.search.adapter.SearchMovieShowVH
import id.apwdevs.app.search.databinding.ItemResultSearchBinding
import id.apwdevs.app.search.ui.SearchFragment
import id.apwdevs.app.search.ui.StateDisplayFragment
import id.apwdevs.app.test.androdtest.BaseAndroidTest
import id.apwdevs.app.test.androdtest.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.Dispatcher
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.`is`
import org.junit.Assert
import org.junit.Test

class SearchCaseTest : BaseAndroidTest() {

    private lateinit var mockDispatcher: SearchMockDispatcher
    private lateinit var searchFragment: SearchFragment

    override fun setup() {
        super.setup()
        mockDispatcher = SearchMockDispatcher(context)
        mockWebServer.dispatcher = SearchMockDispatcher(context)

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
        "text_search".fillViewWithText("a")

        // recyclerview must be not hidden.
        "recyclerView".thisViewMustBeDisplayed()
        "frame_status_container".viewMustBeHidden()

        // check that any data should be exists in recyclerview
        // based on comparing the item displayed, must be not empty!
        "recyclerView".performCheckOnView(
            RecyclerViewCheckItemAssertion { it.adapter?.itemCount != 0 }
        )
    }

    @Test
    fun shouldDisplay_explanation_when_any_error_during_searching() {
        "text_search".fillViewWithText("b")

        "recyclerView".viewMustBeHidden()
        "frame_status_container".thisViewMustBeDisplayed()

        // check if display error displays quickly

        val stateFragments =
            searchFragment.childFragmentManager.fragments[0] as? StateDisplayFragment
        Assert.assertEquals(
            stateFragments?.stateForTests,
            "${StateDisplayFragment::class.java.simpleName}.ERROR"
        )
    }

    @Test
    fun shouldTrigger_search_when_changing_filter_parameters() {
        runBlocking {
            "text_search".fillViewWithText("a")
            "spinner".clickThis()

            "Tv Show".clickThisText()

            // to take the effect, we must delay because ui changes
            delay(150)
            // recyclerview must be not hidden.
            "recyclerView".thisViewMustBeDisplayed()
            "frame_status_container".viewMustBeHidden()

            // check that any data should be exists in recyclerview
            // based on comparing the item displayed, must be not empty!
            "recyclerView".performCheckOnView(
                RecyclerViewCheckItemAssertion { it.adapter?.itemCount != 0 }
            )

            // adult triggers
            // because we not specify adult=true in mock dispatchers, we need to ensure
            // that the lists is empty
            "Adult".clickThisText()

            // to take the effect, we must specify short delay because ui changes
            delay(150)
            "recyclerView".viewMustBeHidden()
            "frame_status_container".thisViewMustBeDisplayed()

            val stateFragments =
                searchFragment.childFragmentManager.fragments[0] as? StateDisplayFragment
            Assert.assertEquals(
                stateFragments?.stateForTests,
                "${StateDisplayFragment::class.java.simpleName}.DATA_EMPTY"
            )
        }

    }

    @Test
    fun shouldAble_to_display_detail_when_clicking_one_item() {
        runBlocking {
            val navController = TestNavHostController(context)

            val ids = id.apwdevs.app.movieshow.R.navigation.main_nav
            searchFragment.apply {
                lifecycleScope.launch(Dispatchers.Main) {
                    navController.setGraph(ids)
                    Navigation.setViewNavController(requireView(), navController)
                }
            }

            "text_search".fillViewWithText("a")
            delay(500)

            // recyclerview must be not hidden.
            "recyclerView".thisViewMustBeDisplayed()
            "frame_status_container".viewMustBeHidden()

            "recyclerView".performActionOnView(
                RecyclerViewRunActionAtPosition<SearchMovieShowVH>(
                    10,
                    {
                        val itemView = it.itemView
                        ItemResultSearchBinding.bind(itemView).detail
                    },
                    click()
                )
            )

            MatcherAssert.assertThat(
                navController.currentDestination?.id,
                `is`(id.apwdevs.app.movieshow.R.id.detailFragment)
            )
        }
    }
}
