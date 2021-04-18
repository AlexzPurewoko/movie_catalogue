package id.apwdevs.app.core.utils

sealed class State<out T>(val data: T?, val error: Throwable?) {
    class Loading() : State<Nothing>(null, null)
    class Success<T>(data: T?) : State<T>(data, null)
    class Error(error: Throwable?) : State<Nothing>(null, error)

    fun <To> copyTo(mapCall: (from: T) -> To): State<To> {
        return when(this) {
            is Loading -> Loading()
            is Error -> Error(error)
            is Success -> Success(data?.let {mapCall(it)})
        }
    }
}