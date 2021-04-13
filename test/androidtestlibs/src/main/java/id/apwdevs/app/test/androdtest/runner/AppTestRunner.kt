package id.apwdevs.app.test.androdtest.runner

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import id.apwdevs.app.test.androdtest.MockApplication

class AppTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, MockApplication::class.java.name, context)
    }
}