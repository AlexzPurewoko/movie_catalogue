package id.apwdevs.app.core.utils

sealed class State<out T>(val data: T?, val error: Throwable?) {
    class Loading() : State<Nothing>(null, null)
    class Success<T>(data: T) : State<T>(data, null)
    class Error(error: Throwable) : State<Nothing>(null, error)
}