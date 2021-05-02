package id.apwdevs.app.detail.ui.case

import android.os.Bundle
import com.google.gson.Gson
import id.apwdevs.app.detail.data.AssetData
import id.apwdevs.app.detail.ui.DetailItemFragment
import id.apwdevs.app.libs.util.readJson
import id.apwdevs.app.res.util.PageType
import id.apwdevs.app.test.androdtest.BaseAndroidTest
import org.junit.Test

abstract class DetailItemFragmentCase : BaseAndroidTest() {

    abstract val defaultPageType: PageType
    abstract val defaultItemId: Int

    /**
     * These test must be overrided by its subclass
     * Because of different behaviour of appearance
     */
    @Test
    abstract fun should_decide_the_right_ui_helper_from_intent()

    @Test
    abstract fun should_display_data_when_success()

    @Test
    abstract fun should_display_error_when_request_failing()

    @Test
    abstract fun should_reload_data_when_click_retry_button()

    @Test
    abstract fun should_favorite_button_hide_and_favoritemenu_show_when_scroll_up()

    @Test
    abstract fun should_change_favorite_icon_after_adding_and_remove_favorite()

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