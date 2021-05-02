package id.apwdevs.app.libs.util

import android.content.Context
import android.util.Log
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

abstract class ScopeDispatcher(
    protected val context: Context,
    private val requestCallbacks: (String) -> Unit = {}
) : Dispatcher() {
    abstract val mappingRequest: Map<String, String>

    override fun dispatch(request: RecordedRequest): MockResponse {
        Log.d("MockDispatcher", "Received Request Path: ${request.path}")
        request.path?.let { requestCallbacks(it) }
        val req = mappingRequest[request.path]
        return req?.let {
            context.provideResponse(it, 200)
        } ?: context.provideResponse(AssetDataJson.DETAIL_404, 404)
    }
}