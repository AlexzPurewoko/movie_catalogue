package id.apwdevs.app.core.utils

sealed class State<T>(private val data: T?, private val error: Throwable?) {
    class Loading() : State<Any>(null, null)
    class Success<T>(data: T) : State<T>(data, null)
    class Error(error: Throwable) : State<Any>(null, error)
}