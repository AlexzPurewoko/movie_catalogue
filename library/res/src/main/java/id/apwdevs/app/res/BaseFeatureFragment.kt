package id.apwdevs.app.res

import android.content.Context
import androidx.fragment.app.Fragment
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module

abstract class BaseFeatureFragment: Fragment() {
    protected abstract val koinModules: List<Module>
    override fun onAttach(context: Context) {
        super.onAttach(context)
        loadKoinModules(koinModules)
    }

    override fun onDetach() {
        super.onDetach()
        unloadKoinModules(koinModules)
    }
}