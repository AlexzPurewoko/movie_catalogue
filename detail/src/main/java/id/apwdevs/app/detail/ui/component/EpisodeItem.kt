package id.apwdevs.app.detail.ui.component

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import id.apwdevs.app.detail.R
import id.apwdevs.app.detail.data.EpisodeItemData
import id.apwdevs.app.detail.databinding.ItemEpisodeContainerBinding
import id.apwdevs.app.res.util.getImageURL

class EpisodeItem: Fragment() {

    private val data: EpisodeItemData by lazy {
        requireArguments().getParcelable(DATA)!!
    }

    private lateinit var binding: ItemEpisodeContainerBinding
    override fun  onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ItemEpisodeContainerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeElements()
    }

    private fun initializeElements() {
        with(binding) {
            title.text = data.title
            Glide.with(requireContext()).load(data.image.getImageURL()).into(episodeImage)
            ratingProgress.progress = data.vote.toFloat()
            secondary.text = getString(R.string.secondary_text, data.seasonNumber, data.date)
        }
    }

    companion object {
        const val DATA = "DATA"

        fun newInstance(data: EpisodeItemData) =
            EpisodeItem().apply {
                arguments = Bundle().also { it.putParcelable(DATA, data) }
            }
    }
}

