package id.apwdevs.app.res.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import id.apwdevs.app.res.BaseFeatureFragment
import id.apwdevs.app.res.fragment.viewmodel.StateViewModel

abstract class FragmentWithState : BaseFeatureFragment() {

    private var referenceStateFragment: StateDisplayFragment? = null
    internal val stateViewModel: StateViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        referenceStateFragment = StateDisplayFragment.newInstance(mapOfTextDisplay())
        stateViewModel.callbackFromStateDisplay.observe(
            viewLifecycleOwner,
            ::provideCallbackFromStateDisplay
        )

    }

    private fun pauseAnim(paused: Boolean){
        referenceStateFragment?.pauseAnim(paused)
    }

    protected fun applyFragmentIntoView(@IdRes resId: Int) {
        childFragmentManager.commit {
            referenceStateFragment?.let {
                add(resId, it, javaClass.simpleName)
            }
        }
    }

    protected open fun toggleStateDisplayFragment(displayed: Boolean) {
        childFragmentManager.commit {
            referenceStateFragment?.let {
                if (displayed) show(it)
                else hide(it)
                pauseAnim(displayed)
            }
        }
    }

    protected fun callStateFragmentToDisplaySomething(displayType: StateViewModel.DisplayType) {
        stateViewModel.callStateFragmentToDisplaySomething(displayType)
    }

    protected abstract fun mapOfTextDisplay(): HashMap<StateViewModel.DisplayType, Int>

    protected abstract fun provideCallbackFromStateDisplay(parameters: List<Any>)

}