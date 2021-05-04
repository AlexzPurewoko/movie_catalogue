package id.apwdevs.app.libs.util

import android.content.Context
import id.apwdevs.app.libs.rule.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import okhttp3.mockwebserver.MockResponse


fun Context.provideResponse(customAssetJson: String, responseCode: Int): MockResponse {

    return MockResponse()
        .setResponseCode(responseCode)
        .setBody(readJson(customAssetJson))
}

fun Context.readJson(path: String): String {
    val iStream = assets.open(path)
    val byteArr = ByteArray(iStream.available())
    iStream.read(byteArr)
    val strResult = String(byteArr)
    iStream.close()
    return strResult
}

@ExperimentalCoroutinesApi
fun runTest(testCoroutineRule: TestCoroutineRule, scope: suspend TestCoroutineScope.() -> Unit) {
    testCoroutineRule.runBlockingTest(scope)
}