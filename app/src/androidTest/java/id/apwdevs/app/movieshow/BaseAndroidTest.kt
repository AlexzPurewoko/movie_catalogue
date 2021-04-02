package id.apwdevs.app.movieshow

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import id.apwdevs.app.detail.dispatcher.DetailScopeMockDispatcher
import okhttp3.mockwebserver.MockWebServer
import id.apwdevs.app.movieshow.provideIdling
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class BaseAndroidTest{
    @get:Rule
    val activityRule: ActivityScenarioRule<FragmentTestActivity> = ActivityScenarioRule(FragmentTestActivity::class.java)

    protected lateinit var mockWebServer: MockWebServer
    protected val context : Context
        get() {
            return InstrumentationRegistry.getInstrumentation().targetContext
        }

    @Before
    fun setup(){
        IdlingRegistry.getInstance().register(provideIdling.idlingSource)
        mockWebServer = MockWebServer()
        mockWebServer.dispatcher = DetailScopeMockDispatcher(context)
        mockWebServer.start(8080)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(provideIdling.idlingSource)
        mockWebServer.dispatcher.shutdown()
        mockWebServer.shutdown()
    }

    protected inline fun <reified TFragment: Fragment> launchFragmentInContainer(
        fragmentClass: Class<TFragment>,
        args: Bundle = Bundle.EMPTY
    ) : TFragment {
        val fragment = fragmentClass.newInstance().apply {
            arguments = args
        }
        activityRule.scenario.onActivity {
            it.setFragment(fragment)
        }
        return fragment
    }
}