package id.apwdevs.app.detail.ui

import android.os.Bundle
import com.google.gson.Gson
import id.apwdevs.app.data.source.remote.response.moviedetail.MovieDetailResponse
import id.apwdevs.app.data.source.remote.response.tvdetail.TvDetailResponse
import id.apwdevs.app.detail.data.AssetData
import id.apwdevs.app.detail.dispatcher.DetailScopeMockDispatcher
import id.apwdevs.app.libs.util.readJson
import id.apwdevs.app.movieshow.util.*
import id.apwdevs.app.res.util.PageType
import id.apwdevs.app.test.androdtest.BaseAndroidTest
import id.apwdevs.app.test.androdtest.utils.*
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Test

/**
 * Test Driven Development
 */

abstract class DetailFragmentBaseCase : BaseAndroidTest() {

    abstract val defaultPageType: PageType
    abstract val defaultItemId: Int

    /**
     * These test must be overrided by its subclass
     * Because of different behaviour of appearance
     */
    @Test
    abstract fun should_decide_the_right_ui_helper_from_intent()

    @Test
    open fun should_display_data_when_success() {
        launchFragment()

        "title".thisViewMustBeDisplayed()
        "poster_image".thisViewMustBeDisplayed()
        "error_display".viewMustBeHidden()
        "favorite_fab".thisViewMustBeDisplayed()

    }

    @Test
    fun should_display_error_when_request_failing() {

        launchFragment(itemId = 76767)
        "error_display".thisViewMustBeDisplayed()
        "error_text".thisViewMustContainTextExactly("Failed to load resource. Try Again ?")
        "btn_retry".thisViewMustBeDisplayed()
    }

    /**
     * Test case, if no internet connection, must display error with button retry
     * then, users click the button.
     */
    @Suppress("IMPLICIT_CAST_TO_ANY")
    @Test
    fun should_reload_data_when_click_retry_button() {

        val data = when (defaultPageType) {
            PageType.MOVIES -> getDefaultData<MovieDetailResponse>()
            PageType.TV_SHOW -> getDefaultData<TvDetailResponse>()
        }

        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(404)
            }
        }
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

    @Test
    fun should_favorite_button_hide_and_favoritemenu_show_when_scroll_up() {
        launchFragment()
        "favorite_fab".thisViewMustBeDisplayed()
        "favorite_menu".viewDoesNotExists()

        // scroll up
        swipeUp()

        "favorite_fab".viewMustBeHidden()
        "favorite_menu".thisViewMustBeDisplayed()
    }

    @Test
    fun should_change_favorite_icon_after_adding_and_remove_favorite() {
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

    protected fun launchFragment(
        pageType: PageType = defaultPageType,
        itemId: Int = defaultItemId
    ): DetailItemFragment {
        return launchFragmentInContainer(
            DetailItemFragment::class.java,
            args = Bundle().apply {
                putParcelable("pageType", pageType)
                putInt("itemId", itemId)
            }
        )
    }

    protected inline fun <reified T> getDefaultData(): T {
        return Gson().fromJson(
            context.readJson(
                when (defaultPageType) {
                    PageType.MOVIES -> AssetData.DETAIL_MOVIE_OK
                    PageType.TV_SHOW -> AssetData.DETAIL_TVSHOW_OK
                }
            ),
            T::class.java
        )
    }
}

