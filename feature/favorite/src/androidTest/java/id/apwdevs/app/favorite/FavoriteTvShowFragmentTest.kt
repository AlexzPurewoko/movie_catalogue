package id.apwdevs.app.favorite

import android.widget.TextView
import androidx.core.view.get
import id.apwdevs.app.data.source.remote.response.tvdetail.TvDetailResponse
import id.apwdevs.app.favorite.testcase.FavoriteFragmentCaseTest
import id.apwdevs.app.favorite.ui.content.FragmentContent
import id.apwdevs.app.res.R
import id.apwdevs.app.res.util.PageType
import id.apwdevs.app.test.androdtest.utils.RecyclerViewCheckItemAssertion
import id.apwdevs.app.test.androdtest.utils.performCheckOnView
import id.apwdevs.app.test.androdtest.utils.thisViewMustBeDisplayed
import id.apwdevs.app.test.androdtest.utils.viewMustBeHidden
import kotlinx.coroutines.runBlocking
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

    private fun launchFragment() {
        launchFragmentInContainer(
            FragmentContent.newInstance(PageType.TV_SHOW)
        )
    }
}