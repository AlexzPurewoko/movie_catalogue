package id.apwdevs.app.detail.ui.helper

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import id.apwdevs.app.detail.R
import id.apwdevs.app.detail.data.TvShowDetail
import id.apwdevs.app.detail.databinding.ContentDetailTvshowBinding
import id.apwdevs.app.detail.ui.adapter.SeasonAdapter
import id.apwdevs.app.detail.ui.component.CardBackdropItem
import id.apwdevs.app.detail.ui.component.EpisodeItem
import id.apwdevs.app.detail.ui.component.NoDataItem
import id.apwdevs.app.detail.util.GlobalLayoutListener
import id.apwdevs.app.res.util.convertRatingFrom10to5
import id.apwdevs.app.res.util.getImageURL
import id.apwdevs.app.res.util.gone
import id.apwdevs.app.res.util.visible


class DetailTvShowHelper(
    private val fragmentManager: FragmentManager,
    onRetry: () -> Unit
) : DetailItemHelper(onRetry) {

    private lateinit var contentBinding: ContentDetailTvshowBinding

    private val sectionFragments: MutableList<Fragment> = mutableListOf()

    override fun initView() {
        contentBinding = ContentDetailTvshowBinding.inflate(
            LayoutInflater.from(rootBinding.root.context),
            rootBinding.nestedScroll,
            false
        )
        contentBinding.root.visibility = View.GONE
        rootBinding.nestedScroll.addView(contentBinding.root)
    }

    @SuppressLint("SetTextI18n")
    override fun onSuccess(data: Any?) {
        val tvShowData = data as TvShowDetail
        val context = rootBinding.root.context
        Glide.with(context)
            .load(tvShowData.posterPath.getImageURL())
            .into(rootBinding.posterImage)
        rootBinding.posterImage.visible()

        with(contentBinding) {
            title.text = tvShowData.title
            ratingBar.rating = tvShowData.rating.convertRatingFrom10to5()
            voteAverageText.text = "(${tvShowData.rating})"
            composeGenre(genres, tvShowData.genres)
            status.text = "${tvShowData.status}\n(${tvShowData.firstAirDate})"
            tvshowType.text = tvShowData.type
            language.text = getLanguage(tvShowData.originalLanguage)
            overview.text = tvShowData.overview
            seasons.adapter = SeasonAdapter(tvShowData.seasons)
            composeFrame(tvShowData)
            root.visible()
        }
    }

    private fun composeFrame(data: TvShowDetail) {
        val nextEpisodeFg =
            data.nextEpisodeToAir?.let { EpisodeItem.newInstance(it) } ?: NoDataItem()
        val lastEpisodeFg = data.lastEpisodeToAir.let { EpisodeItem.newInstance(it) }
        val backdropImg =
            data.backdropPath?.let { CardBackdropItem.newInstance(it.getImageURL()!!, data.title) }
                ?: NoDataItem()

        // apply
        fragmentManager.commit {
            replace(R.id.next_episode, nextEpisodeFg)
            replace(R.id.last_episode, lastEpisodeFg)
            replace(R.id.images_frame, backdropImg)
        }
        sectionFragments.addAll(listOf(lastEpisodeFg, nextEpisodeFg, backdropImg))
    }

    override fun onLoad() {
        contentBinding.root.gone()
    }

    override fun onDestroy() {
        sectionFragments.forEach { fg ->
            if (!fragmentManager.isDestroyed && fragmentManager.fragments.contains(fg))
                fragmentManager.commit { detach(fg) }
        }
        sectionFragments.clear()
    }

    override fun provideGlobalLayoutListener(callback: (Int, Int, Int) -> Unit): GlobalLayoutListener {
        return GlobalLayoutListener(
            rootBinding.root,
            contentBinding.title, contentBinding.ratingBar, contentBinding.genres,
            callback
        )
    }

}