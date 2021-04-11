package id.apwdevs.app.detail.ui

import androidx.appcompat.widget.AppCompatRatingBar
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withResourceName
import androidx.test.ext.junit.runners.AndroidJUnit4
import id.apwdevs.app.data.source.remote.response.moviedetail.MovieDetailResponse
import id.apwdevs.app.detail.ui.helper.DetailMovieHelper
import id.apwdevs.app.movieshow.util.ViewAssertionRunner
import id.apwdevs.app.movieshow.util.mustBeDisplayed
import id.apwdevs.app.movieshow.util.mustDisplayedOnView
import id.apwdevs.app.movieshow.util.swipeUp
import id.apwdevs.app.res.util.PageType
import org.junit.Assert
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class DetailItemMovieCaseTest : DetailFragmentBaseCase() {
    override val defaultPageType: PageType
        get() = PageType.MOVIES
    override val defaultItemId: Int
        get() = 791373

    override fun should_decide_the_right_ui_helper_from_intent() {
        launchFragment(itemId = 587807).apply {
            Assert.assertTrue(detailHelper is DetailMovieHelper)
        }
    }

    override fun should_display_data_when_success() {
        val data = getDefaultData<MovieDetailResponse>()
        super.should_display_data_when_success()
        swipeUp()
        with(data) {
            title.mustDisplayedOnView("title")
            overview!!.mustDisplayedOnView("overview")
            genres.forEach { it.name.mustBeDisplayed() }
            onView(withResourceName("rating_bar")).check(ViewAssertionRunner {
                Assert.assertNotNull(it)
                Assert.assertEquals(voteAverage.toFloat(), (it as AppCompatRatingBar).rating)
            })
        }

    }


}

