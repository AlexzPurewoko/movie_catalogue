package id.apwdevs.app.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.apwdevs.app.res.adapter.MovieShowVH
import id.apwdevs.app.res.data.MovieShowItem
import id.apwdevs.app.res.databinding.ItemShowsBinding

class FavoriteMovieShowAdapter(
    private val listener: (MovieShowItem) -> Unit
) : RecyclerView.Adapter<FavoriteViewHolder>() {

    private val listData: MutableList<MovieShowItem> = mutableListOf()

    fun update(dataColllection: Collection<MovieShowItem>) {
        listData.apply {
            clear()
            addAll(dataColllection)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listData[position], listener)
    }

    override fun getItemCount(): Int = listData.size
}

class FavoriteViewHolder(view: ItemShowsBinding) : MovieShowVH(view) {

    companion object {
        fun create(parent: ViewGroup): FavoriteViewHolder {
            val bindingView = ItemShowsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return FavoriteViewHolder(
                bindingView
            )
        }
    }
}