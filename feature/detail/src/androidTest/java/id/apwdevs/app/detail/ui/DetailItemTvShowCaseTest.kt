package id.apwdevs.app.detail.ui

import androidx.test.ext.junit.runners.AndroidJUnit4
import id.apwdevs.app.detail.ui.helper.DetailTvShowHelper
import id.apwdevs.app.res.util.PageType
import org.junit.Assert
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailItemTvShowCaseTest : DetailFragmentBaseCase() {
    override val defaultPageType: PageType
        get() = PageType.TV_SHOW
    override val defaultItemId: Int
        get() = 88396

    override fun should_decide_the_right_ui_helper_from_intent() {
        launchFragment().apply {
            Assert.assertTrue(detailHelper is DetailTvShowHelper)
        }
    }

    override fun should_display_data_when_success() {
        TODO("Not yet implemented")
    }


}