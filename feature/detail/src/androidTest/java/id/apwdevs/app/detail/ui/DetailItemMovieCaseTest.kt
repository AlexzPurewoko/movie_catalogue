package id.apwdevs.app.detail.ui

import androidx.test.ext.junit.runners.AndroidJUnit4
import id.apwdevs.app.data.source.remote.response.moviedetail.MovieDetailResponse
import id.apwdevs.app.data.source.remote.response.tvdetail.TvDetailResponse
import id.apwdevs.app.detail.dispatcher.DetailScopeMockDispatcher
import id.apwdevs.app.detail.ui.case.DetailItemFragmentCase
import id.apwdevs.app.detail.ui.helper.DetailMovieHelper
import id.apwdevs.app.res.util.PageType
import id.apwdevs.app.test.androdtest.utils.*
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.QueueDispatcher
import org.junit.Assert
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailItemMovieCaseTest : DetailItemFragmentCase() {

    override val defaultPageType: PageType
        get() = PageType.MOVIES
    override val defaultItemId: Int
        get() = 791373

    override fun setup() {
        super.setup()
        mockWebServer.dispatcher = DetailScopeMockDispatcher(context)
    }

    override fun should_decide_the_right_ui_helper_from_intent() {
        launchFragment(itemId = 587807).apply {
            Assert.assertTrue(detailHelper is DetailMovieHelper)
        }
    }

    override fun should_display_data_when_success() {
        val data = getDefaultData<MovieDetailResponse>()
        launchFragment()

        "title".thisViewMustBeDisplayed()
        "poster_image".thisViewMustBeDisplayed()
        "error_display".viewMustBeHidden()
        "favorite_fab".thisViewMustBeDisplayed()

        with(data) {
            title.mustDisplayedOnView("title")
            overview!!.mustDisplayedOnView("overview")
            genres.forEach { it.name.mustBeDisplayed() }
        }
    }

    override fun should_display_error_when_request_failing() {
        mockWebServer.dispatcher = QueueDispatcher()
        mockWebServer.enqueue(MockResponse().setResponseCode(404))
        launchFragment(itemId = 76767)
        "error_display".thisViewMustBeDisplayed()
        "error_text".thisViewMustContainTextExactly("Failed to load resource. Try Again ?")
        "btn_retry".thisViewMustBeDisplayed()
    }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    override fun should_reload_data_when_click_retry_button() {
        val data = when (defaultPageType) {
            PageType.MOVIES -> getDefaultData<MovieDetailResponse>()
            PageType.TV_SHOW -> getDefaultData<TvDetailResponse>()
        }

        mockWebServer.dispatcher = QueueDispatcher()
        mockWebServer.enqueue(MockResponse().setResponseCode(404))
        launchFragment()
        "error_display".thisViewMustBeDisplayed()

        mockWebServer.dispatcher = DetailScopeMockDispatcher(context)

        "btn_retry".clickThis()
        data.let {
            when (it) {
                is MovieDetailResponse -> it.title.mustDisplayedOnView("title")
                is TvDetailResponse -> it.name.mustDisplayedOnView("title")
            }
        }
    }

    override fun should_favorite_button_hide_and_favoritemenu_show_when_scroll_up() {
        mockWebServer.dispatcher = DetailScopeMockDispatcher(context)

        launchFragment()
        "favorite_fab".thisViewMustBeDisplayed()
        "favorite_menu".viewDoesNotExists()

        // scroll up
        swipeUp()

        "favorite_fab".viewMustBeHidden()
        "favorite_menu".thisViewMustBeDisplayed()
    }

    override fun should_change_favorite_icon_after_adding_and_remove_favorite() {
        mockWebServer.dispatcher = DetailScopeMockDispatcher(context)

        launchFragment()
        // add to favorite
        "favorite_fab".thisViewMustBeDisplayed()
        "favorite_fab".clickThis()

        // check, is the favorite ever shown, or toggle drawable changed ?
        // for image view test, we need to check tag state on image view.
        "favorite_fab".mustContainTagsExactly("favorited")

        // cleanup
        "favorite_fab".clickThis()
        "favorite_fab".mustContainTagsExactly("unfavorited")
    }


}
