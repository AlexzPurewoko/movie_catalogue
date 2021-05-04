package id.apwdevs.app.res.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import id.apwdevs.app.core.domain.model.Genre
import id.apwdevs.app.res.R
import id.apwdevs.app.res.data.MovieShowItem
import id.apwdevs.app.res.databinding.ItemShowsBinding
import id.apwdevs.app.res.util.backdropImageUrlPath
import kotlin.random.Random

class ListMovieShowAdapter(
    private val listener: (MovieShowItem) -> Unit
) : PagingDataAdapter<MovieShowItem, MovieShowVH>(COMPARATOR) {

    override fun onBindViewHolder(holder: MovieShowVH, position: Int) {
        getItem(position)?.let { holder.bind(it, listener) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieShowVH {
        return MovieShowVH.create(parent)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<MovieShowItem>() {
            override fun areItemsTheSame(oldItem: MovieShowItem, newItem: MovieShowItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: MovieShowItem,
                newItem: MovieShowItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}

open class MovieShowVH(
    private val views: ItemShowsBinding
) : RecyclerView.ViewHolder(views.root) {

    fun bind(item: MovieShowItem, listener: (MovieShowItem) -> Unit) {
        views.apply {
            title.text = item.title
            overview.text = item.overview
            textProgress.text = item.voteAverage.toString()
            generateGenres(genres, item.genres)
            setRatingProgress(ratingProgress, item.voteAverage)
            randomizeFont(title)
            showImg(backdropImg, item.backdopImage)
            addClickable(item, listener)
        }
    }

    private fun addClickable(item: MovieShowItem, listener: (MovieShowItem) -> Unit) {
        views.imageView.setOnClickListener { listener(item) }
    }

    private fun generateGenres(genresGroup: ChipGroup, genreCollection: List<Genre>) {
        if (genresGroup.childCount != 0) return
        genreCollection.forEach {
            val chip = Chip(genresGroup.context)
            chip.text = it.genreName
            genresGroup.addView(chip)
        }
    }

    private fun setRatingProgress(ratingProgress: CircularProgressBar, voteAverage: Double) {
        ratingProgress.progress = voteAverage.toFloat()
        ratingProgress.progressMax = 10f
    }

    private fun showImg(backdropImg: AppCompatImageView, backdopImage: String?) {
        Glide.with(backdropImg.context).load(backdopImage?.backdropImageUrlPath)
            .placeholder(R.drawable.landscape_placeholder_image).into(backdropImg)
    }

    private fun randomizeFont(text: AppCompatTextView) {
        val fonts = listOf(
            R.font.dancingscript,
            R.font.indieflower,
            R.font.shadowsintolight
        )
        val randomized = Random.Default.nextInt(3)
        text.typeface =
            ResourcesCompat.getFont(text.context, fonts[randomized])
    }

    companion object {
        fun create(parent: ViewGroup): MovieShowVH {

            val bindingView = ItemShowsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return MovieShowVH(
                bindingView
            )
        }
    }
}



