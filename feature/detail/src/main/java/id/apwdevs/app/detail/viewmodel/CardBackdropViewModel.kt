package id.apwdevs.app.detail.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import androidx.annotation.WorkerThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import id.apwdevs.app.movieshow.R
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.ExecutionException

class CardBackdropViewModel(app: Application) : AndroidViewModel(app) {

    private val appContext: Context
        get() = getApplication() as Context

    private val _status = MutableLiveData<Status>()

    val status: LiveData<Status>
        get() = _status

    fun save(imageUrl: String, titleMovie: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _status.postValue(Status.STARTING)
            getImg(imageUrl).collect {
                if (it != null) {
                    val pathFile = composePath(titleMovie)
                    saveImage(it, pathFile)
                    _status.postValue(Status.SUCCESS)
                } else {
                    _status.postValue(Status.FAILED)
                }
            }
        }

    }

    private fun composePath(titleMovie: String): File {
        val externalFilesDir = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val filePath = File(externalFilesDir, appContext.getString(R.string.app_name))
        filePath.mkdirs()
        return File(filePath, "$titleMovie.jpeg")
    }

    private fun saveImage(bitmap: Bitmap, file: File) {
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream.close()
        bitmap.recycle()
    }

    @WorkerThread
    @Throws(CancellationException::class, ExecutionException::class)
    private fun getImg(imageUrl: String): Flow<Bitmap?> = flow {
        var bitmap: Bitmap? = null

        try {
            bitmap = Glide.with(appContext).asBitmap()
                .load(imageUrl)
                .skipMemoryCache(true)
                .submit()
                .get()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        emit(bitmap)
    }

    enum class Status {
        FAILED,
        SUCCESS,
        STARTING
    }
}

class ImageStorageHandler {

}