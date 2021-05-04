package id.apwdevs.app.detail.ui.helper

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import id.apwdevs.app.detail.R
import id.apwdevs.app.detail.data.MovieDetail
import id.apwdevs.app.detail.databinding.ContentDetailMovieBinding
import id.apwdevs.app.detail.ui.component.CardBackdropItem
import id.apwdevs.app.detail.ui.component.NoDataItem
import id.apwdevs.app.res.util.convertRatingFrom10to5
import id.apwdevs.app.res.util.getImageURL
import id.apwdevs.app.res.util.gone
import id.apwdevs.app.res.util.visible
import id.apwdevs.app.res.R as Res
class DetailMovieHelper(private val fragmentManager: FragmentManager, onRetry: () -> Unit) :
    DetailItemHelper(onRetry) {

    private lateinit var contentBinding: ContentDetailMovieBinding
    private var backdropImageSection: Fragment? = null

    override fun initView() {
        contentBinding = ContentDetailMovieBinding.inflate(
            LayoutInflater.from(rootBinding.root.context),
            rootBinding.nestedScroll,
            false
        )
        contentBinding.root.visibility = View.GONE
        rootBinding.nestedScroll.addView(contentBinding.root)
    }

    @SuppressLint("SetTextI18n")
    override fun onSuccess(data: Any?) {
        val movieData = data as MovieDetail
        val context = rootBinding.root.context
        Glide.with(context)
            .load(movieData.posterPath.getImageURL())
            .placeholder(Res.drawable.potrait_loading_placeholder)
            .into(rootBinding.posterImage)
        rootBinding.posterImage.visible()

        with(contentBinding) {
            title.text = movieData.title
            ratingBar.rating = movieData.rating.convertRatingFrom10to5()
            Log.e("Rating", "rating: ${movieData.rating}")
            voteAverageText.text = "(${movieData.rating})"
            composeGenre(genres, movieData.genres)
            status.text = "${movieData.status}\n(${movieData.releaseDate})"
            runtime.text = movieData.runTime
            language.text = getLanguage(movieData.originalLanguage)
            overview.text = movieData.overview
            composeBackdrop(movieData.backdropPath, movieData.title)
            root.visible()
        }
    }

    override fun onLoad() {
        contentBinding.root.gone()
    }

    override fun onDestroy() {
        backdropImageSection?.let {
            if (!fragmentManager.isDestroyed && fragmentManager.fragments.contains(it))
                fragmentManager.commit {
                    detach(it)
                }
        }
        backdropImageSection = null
    }

    private fun composeBackdrop(backdropPath: String?, titleMovie: String) {

        backdropImageSection = backdropPath?.let {
            CardBackdropItem.newInstance(it.getImageURL()!!, titleMovie)
        } ?: NoDataItem()

        fragmentManager.commit {
            replace(R.id.images_frame, backdropImageSection!!)
        }

    }


}