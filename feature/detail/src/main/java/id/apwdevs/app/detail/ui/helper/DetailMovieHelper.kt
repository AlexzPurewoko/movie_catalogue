package id.apwdevs.app.detail.ui.helper

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import id.apwdevs.app.detail.R
import id.apwdevs.app.detail.data.MovieDetail
import id.apwdevs.app.detail.databinding.ContentDetailMovieBinding
import id.apwdevs.app.detail.ui.component.CardBackdropItem
import id.apwdevs.app.detail.ui.component.NoDataItem
import id.apwdevs.app.detail.util.GlobalLayoutListener
import id.apwdevs.app.res.util.convertRatingFrom10to5
import id.apwdevs.app.res.util.getImageURL
import id.apwdevs.app.res.util.gone
import id.apwdevs.app.res.util.visible
import id.apwdevs.app.res.R as Res

class DetailMovieHelper(private val fragmentManager: FragmentManager, onRetry: () -> Unit) :
    DetailItemHelper(onRetry) {

    private var contentBinding: ContentDetailMovieBinding? = null

    override fun initView() {
        rootBinding?.let { root ->
            contentBinding = ContentDetailMovieBinding.inflate(
                LayoutInflater.from(root.root.context),
                root.nestedScroll,
                false
            )
            contentBinding?.root?.visibility = View.GONE
            root.nestedScroll.addView(contentBinding?.root)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onSuccess(data: Any?) {
        val movieData = data as MovieDetail

        rootBinding?.apply {
            Glide.with(root.context)
                .load(movieData.posterPath.getImageURL())
                .placeholder(Res.drawable.potrait_loading_placeholder)
                .into(posterImage)
            posterImage.visible()
        }

        contentBinding?.apply {
            title.text = movieData.title
            ratingBar.rating = movieData.rating.convertRatingFrom10to5()
            Log.e("Rating", "rating: ${movieData.rating}")
            voteAverageText.text = "(${movieData.rating})"
            composeGenre(genres, movieData.genres)
            status.text = "${movieData.status}\n(${movieData.releaseDate})"
            runtime.text = movieData.runTime
            language.text = movieData.originalLanguage
            overview.text = movieData.overview
            composeBackdrop(movieData.backdropPath, movieData.title)
            root.visible()
        }
    }


    override fun onLoad() {
        contentBinding?.root?.gone()
    }

    override fun onDestroy() {}

    override fun provideGlobalLayoutListener(callback: (Int, Int, Int) -> Unit): GlobalLayoutListener? {
        return rootBinding?.let { rootBinding ->
            contentBinding?.let {  contentBinding ->
                GlobalLayoutListener(
                    rootBinding.root,
                    contentBinding.title, contentBinding.ratingBar, contentBinding.genres,
                    callback
                )
            }
        }
    }

    private fun composeBackdrop(backdropPath: String?, titleMovie: String) {

        /**
         * I need to use double bang. And ensure that everything
         * has been checked, possible not receive NPE
         */
        val backdropImageSection = backdropPath?.let {
            CardBackdropItem.newInstance(it.getImageURL()!!, titleMovie)
        } ?: NoDataItem()

        fragmentManager.commit {
            disallowAddToBackStack()
            replace(R.id.images_frame, backdropImageSection)
        }

    }


}

