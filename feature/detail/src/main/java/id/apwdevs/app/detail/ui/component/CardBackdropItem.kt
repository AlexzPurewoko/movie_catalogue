package id.apwdevs.app.detail.ui.component

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import id.apwdevs.app.detail.databinding.CardBackdropImageBinding
import id.apwdevs.app.detail.viewmodel.CardBackdropViewModel
import id.apwdevs.app.detail.viewmodel.CardBackdropViewModel.Status

class CardBackdropItem : Fragment() {

    private var binding: CardBackdropImageBinding? = null

    private val vm: CardBackdropViewModel by viewModels()//by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CardBackdropImageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showImage()
        vm.status.observe(viewLifecycleOwner, this::statusSaveFile)
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        context?.let {
//            binding?.backdropImage?.let{ storeImg ->
//                Glide.with(storeImg).
//            }
//        }
        binding = null
    }

    private fun statusSaveFile(status: Status) {
        binding?.saveBtn?.let { saveBtn ->
            saveBtn.isEnabled = status != Status.STARTING
        }

    }

    private fun showImage() {
        binding?.apply {
            context?.let { Glide.with(it)
                .load(arguments?.getString(STRING_DATA))
                .into(backdropImage) }

            saveBtn.setOnClickListener { saveImage() }
        }
    }

    private fun saveImage() {
        val imageUrl = arguments?.getString(STRING_DATA)
        val titleStr = arguments?.getString(TITLE_STRING)
        if (imageUrl != null && titleStr != null) {
            vm.save(imageUrl, titleStr)
        }
    }

    companion object {
        const val STRING_DATA = "IMAGE_URL"
        const val TITLE_STRING = "TITLE_DETAIL"

        @JvmStatic
        fun newInstance(data: String, titleMovie: String): CardBackdropItem {
            Log.e("DATA", "data: $data, title: $titleMovie")
            return CardBackdropItem().apply {
                arguments = Bundle().also {
                    it.putString(STRING_DATA, data)
                    it.putString(TITLE_STRING, titleMovie)
                }
            }
        }
    }
}