package id.apwdevs.app.test.androdtest

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import id.apwdevs.app.movieshow.FragmentTestActivity
import id.apwdevs.app.movieshow.provideIdling
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule


abstract class BaseAndroidTest {
    @get:Rule
    val activityRule: ActivityScenarioRule<FragmentTestActivity> = ActivityScenarioRule(
        FragmentTestActivity::class.java
    )

    protected lateinit var mockWebServer: MockWebServer

    protected val context: Context
        get() {
            return InstrumentationRegistry.getInstrumentation().targetContext
        }

    @Before
    open fun setup() {
        IdlingRegistry.getInstance().register(provideIdling.idlingSource)
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
    }

    @After
    open fun tearDown() {
        IdlingRegistry.getInstance().unregister(provideIdling.idlingSource)
        mockWebServer.dispatcher.shutdown()
        mockWebServer.shutdown()
    }

    protected inline fun <reified TFragment : Fragment> launchFragmentInContainer(
        fragmentClass: Class<TFragment>,
        args: Bundle = Bundle.EMPTY
    ): TFragment {
        val fragment = fragmentClass.newInstance().apply {
            arguments = args
        }
        activityRule.scenario.onActivity {
            it.setFragment(fragment)
        }
        return fragment
    }

    protected inline fun <reified TFragment : Fragment> launchFragmentInContainer(
        fragment: TFragment
    ): TFragment {
        activityRule.scenario.onActivity {
            it.setFragment(fragment)
        }
        return fragment
    }
}