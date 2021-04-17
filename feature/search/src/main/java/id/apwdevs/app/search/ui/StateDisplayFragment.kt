package id.apwdevs.app.search.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import id.apwdevs.app.search.R
import id.apwdevs.app.search.databinding.StateFragmentBinding

class StateDisplayFragment : Fragment() {

    private lateinit var binding: StateFragmentBinding
    private var viewModelShare: StateViewModel? = null

    @VisibleForTesting
    var stateForTests: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModelShare = (parentFragment as SearchFragment).stateViewModel
        requireNotNull(viewModelShare) { "You must provide public variable stateViewModel in your fragment" }
    }

    override fun onDetach() {
        super.onDetach()
        viewModelShare = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = StateFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tag
        binding.btnRetry.setOnClickListener(::onRetryClick)
        viewModelShare?.stateFragmentDisplay?.observe(viewLifecycleOwner, ::stateDisplayObserver)
    }

    private fun stateDisplayObserver(displayState: StateViewModel.DisplayType) {
        var displayText = ""
        when (displayState) {
            StateViewModel.DisplayType.RECOMMENDATION -> {
                changeAnim(ANIMATION_RECOMMENDATION)
                displayText = getString(R.string.display_recommendation)
                toggleViewButton(false)
            }
            StateViewModel.DisplayType.ERROR -> {
                changeAnim(ANIMATION_ERROR)
                displayText = getString(R.string.display_error)
                toggleViewButton(true)
            }
            StateViewModel.DisplayType.LOADING -> {
                changeAnim(ANIMATION_LOADING)
                toggleViewButton(false)
            }
            StateViewModel.DisplayType.DATA_EMPTY -> {
                changeAnim(ANIMATION_DATA_EMPTY)
                displayText = getString(R.string.display_empty)
                toggleViewButton(true)
            }
        }
        stateForTests = "${javaClass.simpleName}.$displayState"
        binding.titleExplanation.text = displayText
    }

    private fun toggleViewButton(displayed: Boolean) {
        binding.btnRetry.visibility = if (displayed) View.VISIBLE else View.INVISIBLE
    }

    private fun onRetryClick(v: View) {
        viewModelShare?.callToMainFragment(StateViewModel.StateCallType.RETRY)
    }

    private fun changeAnim(anim: String) {
        binding.lottieAnim.apply {
            pauseAnimation()
            setAnimation(anim)
            playAnimation()
        }
    }

    companion object {
        private const val ANIMATION_DATA_EMPTY = "empty_box.json"
        private const val ANIMATION_ERROR = "display_error.json"
        private const val ANIMATION_LOADING = "search_process.json"
        private const val ANIMATION_RECOMMENDATION = "search_recommendation_lottie.json"
    }
}