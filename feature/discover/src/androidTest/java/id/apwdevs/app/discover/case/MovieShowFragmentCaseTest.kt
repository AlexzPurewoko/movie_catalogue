package id.apwdevs.app.discover.case

import id.apwdevs.app.test.androdtest.BaseAndroidTest
import org.junit.Test

abstract class MovieShowFragmentCaseTest: BaseAndroidTest() {

    @Test
    fun should_display_minimal_ui() {

    }

    @Test
    abstract fun should_display_data_when_connected_to_internet()

    @Test
    abstract fun should_display_empty_data_when_receive_empty_data()

    @Test
    abstract fun should_display_error_when_not_connected_to_internet()

    @Test
    abstract fun should_be_able_to_refresh_data_after_clicking_try_again()
}