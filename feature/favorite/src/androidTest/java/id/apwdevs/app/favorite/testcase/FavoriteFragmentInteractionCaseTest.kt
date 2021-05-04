package id.apwdevs.app.favorite.testcase

import id.apwdevs.app.test.androdtest.BaseAndroidTest
import org.junit.Test

abstract class FavoriteFragmentInteractionCaseTest : BaseAndroidTest() {

    @Test
    abstract fun should_display_correct_ui()

    @Test
    abstract fun should_display_correct_fragment_when_swiping()

    @Test
    abstract fun should_display_correct_fragment_when_clicking_tabs()

}