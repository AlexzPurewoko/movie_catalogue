package id.apwdevs.app.data.util

import android.content.Context
import okhttp3.mockwebserver.MockResponse


fun Context.provideResponse(customAssetJson: String, responseCode: Int) : MockResponse {

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