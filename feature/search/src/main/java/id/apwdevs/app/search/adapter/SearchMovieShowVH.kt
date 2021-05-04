package id.apwdevs.app.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import id.apwdevs.app.res.util.backdropImageUrlPath
import id.apwdevs.app.search.databinding.ItemResultSearchBinding
import id.apwdevs.app.search.model.SearchItem
import id.apwdevs.app.res.R as Res

class SearchMovieShowVH(
    private val binding: ItemResultSearchBinding
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(item: SearchItem, listener: (SearchItem) -> Unit) {
        with(binding) {
            title.text = item.title
            date.text = item.releaseDate
            textProgress.text = item.voteAverage.toString()
            addClickable(detail, item, listener)
            showImg(backdropImg, item.backdropImage)
            setRatingProgress(ratingProgress, item.voteAverage)
        }
    }

    private fun setRatingProgress(ratingProgress: CircularProgressBar, voteAverage: Double) {
        ratingProgress.progress = voteAverage.toFloat()
        ratingProgress.progressMax = 10f
    }

    private fun addClickable(detail: ImageView, item: SearchItem, listener: (SearchItem) -> Unit) {
        detail.setOnClickListener { listener(item) }
    }

    private fun showImg(backdropImg: AppCompatImageView, backdopImage: String?) {
        Glide.with(backdropImg.context).load(backdopImage?.backdropImageUrlPath)
            .placeholder(Res.drawable.landscape_placeholder_image)
            .into(backdropImg)
    }

    companion object {
        fun create(parent: ViewGroup): SearchMovieShowVH {

            val bindingView = ItemResultSearchBinding.inflate(
                LayoutInflater.from(parent.context)
            )
            bindingView.root.layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
            return SearchMovieShowVH(
                bindingView
            )
        }
    }
}