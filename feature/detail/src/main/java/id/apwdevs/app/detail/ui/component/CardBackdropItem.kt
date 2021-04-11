package id.apwdevs.app.detail.ui.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import id.apwdevs.app.detail.databinding.CardBackdropImageBinding
import id.apwdevs.app.detail.viewmodel.CardBackdropViewModel
import id.apwdevs.app.detail.viewmodel.CardBackdropViewModel.Status
import org.koin.android.viewmodel.ext.android.viewModel

class CardBackdropItem : Fragment() {

    private lateinit var binding: CardBackdropImageBinding

    private val imageUrl: String by lazy {
        requireArguments().getString(STRING_DATA)!!
    }
    private val vm: CardBackdropViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CardBackdropImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showImage()
        vm.status.observe(viewLifecycleOwner, this::statusSaveFile)
    }

    private fun statusSaveFile(status: Status) {
        when (status) {
            Status.FAILED -> {
                binding.saveBtn.isEnabled = true
            }
            Status.SUCCESS -> {
                binding.saveBtn.isEnabled = true
            }
            Status.STARTING -> {
                binding.saveBtn.isEnabled = false
            }
        }
    }

    private fun showImage() {
        Glide.with(requireContext())
            .load(requireArguments().getString(STRING_DATA))
            .into(binding.backdropImage)
        binding.saveBtn.setOnClickListener(this::saveImage)
    }

    private fun saveImage(v: View) {
        vm.save(imageUrl, requireArguments().getString(TITLE_STRING)!!)
    }

    companion object {
        const val STRING_DATA = "IMAGE_URL"
        const val TITLE_STRING = "TITLE_DETAIL"

        @JvmStatic
        fun newInstance(data: String, titleMovie: String): CardBackdropItem {
            return CardBackdropItem().apply {
                arguments = Bundle().also {
                    it.putString(STRING_DATA, data)
                    it.putString(TITLE_STRING, titleMovie)
                }
            }
        }
    }
}