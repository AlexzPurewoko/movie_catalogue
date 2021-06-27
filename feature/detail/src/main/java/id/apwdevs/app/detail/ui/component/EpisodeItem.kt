package id.apwdevs.app.detail.ui.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import id.apwdevs.app.detail.R
import id.apwdevs.app.detail.data.EpisodeItemData
import id.apwdevs.app.detail.databinding.ItemEpisodeContainerBinding
import id.apwdevs.app.res.R.drawable.landscape_placeholder_image
import id.apwdevs.app.res.util.getImageURL

class EpisodeItem : Fragment() {

    private val data: EpisodeItemData by lazy {
        requireArguments().getParcelable(DATA)!!
    }

    private var binding: ItemEpisodeContainerBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ItemEpisodeContainerBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeElements()
    }

    private fun initializeElements() {
        binding?.apply {
            title.text = data.title
            Glide.with(requireContext()).load(data.image.getImageURL())
                .placeholder(landscape_placeholder_image).into(episodeImage)
            ratingProgress.progress = data.vote.toFloat()
            secondary.text = getString(R.string.secondary_text, data.seasonNumber, data.date)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        const val DATA = "DATA"

        fun newInstance(data: EpisodeItemData) =
            EpisodeItem().apply {
                arguments = Bundle().also { it.putParcelable(DATA, data) }
            }
    }
}

