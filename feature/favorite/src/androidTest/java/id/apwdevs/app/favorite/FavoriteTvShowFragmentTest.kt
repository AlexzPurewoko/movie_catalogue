package id.apwdevs.app.favorite

import android.widget.TextView
import androidx.core.view.get
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.action.ViewActions.click
import id.apwdevs.app.data.source.remote.response.tvdetail.TvDetailResponse
import id.apwdevs.app.favorite.adapter.FavoriteViewHolder
import id.apwdevs.app.favorite.testcase.FavoriteFragmentCaseTest
import id.apwdevs.app.favorite.ui.content.FragmentContent
import id.apwdevs.app.res.R
import id.apwdevs.app.res.databinding.ItemShowsBinding
import id.apwdevs.app.res.util.PageType
import id.apwdevs.app.test.androdtest.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Assert

class FavoriteTvShowFragmentTest: FavoriteFragmentCaseTest() {

    override fun should_display_no_data_if_not_have_data_in_database() {
        runBlocking {
            launchFragment()

            "recyclerView".viewMustBeHidden()
            "frame_status".thisViewMustBeDisplayed()
        }
    }

    override fun should_display_data_if_have_any_data_in_database() {
        runBlocking {
            val expectedResponse = prepopulateDataInMemoryDatabase(PageType.TV_SHOW) as TvDetailResponse

            launchFragment()

            "recyclerView".thisViewMustBeDisplayed()
            "frame_status".viewMustBeHidden()

            // must exactly match the data stored in the database.
            "recyclerView".performCheckOnView(
                RecyclerViewCheckItemAssertion {
                    Assert.assertEquals(1, it.adapter?.itemCount)

                    val actualItemName = it[0].findViewById<TextView>(R.id.title).text.toString()
                    Assert.assertEquals(expectedResponse.name, actualItemName)
                }
            )
        }
    }

    override fun should_display_no_data_whereby_any_data_but_not_to_display_in_this_page() {
        runBlocking {
            prepopulateDataInMemoryDatabase(PageType.MOVIES)

            launchFragment()

            "recyclerView".viewMustBeHidden()
            "frame_status".thisViewMustBeDisplayed()
        }
    }

    override fun can_move_to_next_page_when_clicking_next_button() {
        runBlocking {
            prepopulateDataInMemoryDatabase(PageType.TV_SHOW)

            val fg = launchFragment()
            val ids = id.apwdevs.app.movieshow.R.navigation.main_nav
            val navController = TestNavHostController(context)

            launch(Dispatchers.Main) {
                fg.apply {
                    navController.setGraph(ids)
                    Navigation.setViewNavController(requireView(), navController)
                }
            }.join()

            "recyclerView".performActionOnView(
                RecyclerViewRunActionAtPosition<FavoriteViewHolder>(
                    0,
                    {
                        val v = it.itemView
                        ItemShowsBinding.bind(v).imageView
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

    private fun launchFragment(): FragmentContent {
        return launchFragmentInContainer(
            FragmentContent.newInstance(PageType.TV_SHOW)
        )
    }
}