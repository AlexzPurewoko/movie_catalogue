package id.apwdevs.app.core.utils

sealed class State<T>(val data: T?, val error: Throwable?) {
    class Loading<T>() : State<T>(null, null)
    class Success<T>(data: T) : State<T>(data, null)
    class Error<T>(error: Throwable) : State<T>(null, error)
}