package id.apwdevs.app.movieshow.base

import android.app.Application
import androidx.lifecycle.*
import id.apwdevs.app.movieshow.MainApplication
import id.apwdevs.app.movieshow.util.IdlingResourceHelper
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.CoroutineContext

open class BaseViewModel(
    protected val application: Application
): ViewModel() {
    private val idlingRes: IdlingResourceHelper = (application as MainApplication).idlingRes

    @Suppress("unused")
    protected fun safelyExecuteTask(dispatcher: CoroutineDispatcher = Dispatchers.Default, block: suspend CoroutineScope.() -> Unit){
        viewModelScope.launch {
            idlingRes.increment()
            block.invoke(this)
            idlingRes.decrement()
        }
    }

    protected fun <T> Flow<T>.toLiveData(
        context: CoroutineContext,
        timeoutInMs: Long = DEFAULT_TIMEOUT
    ): LiveData<T> = liveData(context, timeoutInMs) {

        collect {
            idlingRes.increment()
            emit(it)
            idlingRes.decrement()
        }
    }

    companion object {
        private const val DEFAULT_TIMEOUT = 5000L
    }
}