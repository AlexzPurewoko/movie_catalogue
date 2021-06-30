package id.apwdevs.app.libs.util

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.apwdevs.app.data.source.remote.response.MovieItemResponse
import id.apwdevs.app.data.source.remote.response.TvShowItemResponse

class RecyclerTestAdapter<T : Any> : PagingDataAdapter<T, VH<T>>(DiffRecyclerTest()) {
    override fun onBindViewHolder(holder: VH<T>, position: Int) {
        holder.bind(getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<T> {
        return VH(View(parent.context))
    }

    fun forcePrefetch(position: Int) {
        val item = getItem(position)
        if (item is TvShowItemResponse) {
            Log.d("RESULT", "id: ${item.id}, name: ${item.name}")
        }
    }
}

class DiffRecyclerTest<T> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return when (oldItem) {
            is MovieItemResponse -> oldItem.id == (newItem as MovieItemResponse).id
            is TvShowItemResponse -> oldItem.id == (newItem as TvShowItemResponse).id
            else -> false
        }
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

}

class VH<T>(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(data: T) {}
}