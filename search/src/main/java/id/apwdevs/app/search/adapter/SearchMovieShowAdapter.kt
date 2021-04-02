package id.apwdevs.app.search.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import id.apwdevs.app.search.model.SearchItem

class SearchMovieShowAdapter(
    private val listener: (SearchItem) -> Unit
): PagingDataAdapter<SearchItem, SearchMovieShowVH>(COMPARATOR) {

    override fun onBindViewHolder(holder: SearchMovieShowVH, position: Int) {
        getItem(position)?.let { holder.bind(it, listener) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMovieShowVH {
        return SearchMovieShowVH.create(parent)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<SearchItem>() {
            override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: SearchItem,
                newItem: SearchItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}

