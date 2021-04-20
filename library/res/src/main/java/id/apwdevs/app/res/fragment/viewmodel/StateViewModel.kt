package id.apwdevs.app.res.fragment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StateViewModel : ViewModel() {
    // backing fields
    private val _stateDisplay = MutableLiveData<DisplayType>()

    // first parameter of statedisplay indicates call types
    private val _callFromStateDisplay = MutableLiveData<List<Any>>()

    // accessor
    val stateFragmentDisplay: LiveData<DisplayType> = _stateDisplay
    val callbackFromStateDisplay: LiveData<List<Any>> = _callFromStateDisplay

    fun callStateFragmentToDisplaySomething(displayType: DisplayType) {
        _stateDisplay.value = displayType
    }

    fun callToMainFragment(vararg any: Any) {
        _callFromStateDisplay.value = any.toList()
    }

    enum class DisplayType {
        RECOMMENDATION,
        ERROR,
        LOADING,
        DATA_EMPTY
    }

    enum class StateCallType {
        RETRY,
        OTHERS
    }
}