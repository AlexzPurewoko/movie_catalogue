package id.apwdevs.app.movieshow
import androidx.test.espresso.idling.CountingIdlingResource
import id.apwdevs.app.movieshow.util.IdlingResourceHelper

val provideIdling by lazy { IdlingResourceImpl() }

class IdlingResourceImpl: IdlingResourceHelper() {
    val idlingSource = CountingIdlingResource("appTest")
    override fun increment() {
        idlingSource.increment()
    }

    override fun decrement() {
        idlingSource.decrement()
    }
}