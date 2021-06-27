package id.apwdevs.app.res.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import id.apwdevs.app.res.databinding.StateFragmentBinding
import id.apwdevs.app.res.fragment.viewmodel.StateViewModel

class StateDisplayFragment : Fragment() {

    private var binding: StateFragmentBinding? = null
    private var viewModelShare: StateViewModel? = null

    private var textMaps: HashMap<StateViewModel.DisplayType, Int>? = null

    @VisibleForTesting
    var stateForTests: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModelShare = (parentFragment as FragmentWithState).stateViewModel
        requireNotNull(viewModelShare) { "You must provide public variable stateViewModel in your fragment" }
    }

    override fun onDetach() {
        super.onDetach()
        viewModelShare = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StateFragmentBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        instantiateMaps(savedInstanceState)
        binding?.btnRetry?.setOnClickListener { onRetryClick() }
        viewModelShare?.stateFragmentDisplay?.observe(viewLifecycleOwner, ::stateDisplayObserver)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(DISPLAY_TEXT_MAPS_KEY, textMaps)
    }

    private fun instantiateMaps(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val savedMaps =
                savedInstanceState.getSerializable(DISPLAY_TEXT_MAPS_KEY) as? HashMap<StateViewModel.DisplayType, Int>
            if (savedMaps != null) {
                textMaps = savedMaps
                return
            }
        }

        arguments?.let {
            textMaps =
                it.getSerializable(DISPLAY_TEXT_MAPS_KEY) as? HashMap<StateViewModel.DisplayType, Int>
        }
    }

    private fun stateDisplayObserver(displayState: StateViewModel.DisplayType) {

        val toggleButton = when (displayState) {
            StateViewModel.DisplayType.RECOMMENDATION, StateViewModel.DisplayType.LOADING ->
                false
            else -> true
        }
        applyDisplay(displayState, toggleButton)
    }

    private fun applyDisplay(displayState: StateViewModel.DisplayType, toggleButton: Boolean) {
        changeAnim(
            when (displayState) {
                StateViewModel.DisplayType.RECOMMENDATION -> ANIMATION_RECOMMENDATION
                StateViewModel.DisplayType.ERROR -> ANIMATION_ERROR
                StateViewModel.DisplayType.LOADING -> ANIMATION_LOADING
                StateViewModel.DisplayType.DATA_EMPTY -> ANIMATION_DATA_EMPTY
            }
        )
        textMaps?.get(displayState)?.let { binding?.titleExplanation?.text = getString(it) }

        toggleViewButton(toggleButton)
        stateForTests = "${javaClass.simpleName}.$displayState"
    }

    private fun toggleViewButton(displayed: Boolean) {
        binding?.btnRetry?.visibility = if (displayed) View.VISIBLE else View.INVISIBLE
    }

    private fun onRetryClick() {
        viewModelShare?.callToMainFragment(StateViewModel.StateCallType.RETRY)
    }

    private fun changeAnim(anim: String) {
        binding?.lottieAnim?.apply {
            pauseAnimation()
            setAnimation(anim)
            playAnimation()
        }
    }

    fun pauseAnim(paused: Boolean) {
        binding?.lottieAnim?.apply {
            if(paused) pauseAnimation()
            else playAnimation()
        }
    }

    companion object {
        private const val ANIMATION_DATA_EMPTY = "empty_box.json"
        private const val ANIMATION_ERROR = "display_error.json"
        private const val ANIMATION_LOADING = "search_process.json"
        private const val ANIMATION_RECOMMENDATION = "search_recommendation_lottie.json"

        private const val DISPLAY_TEXT_MAPS_KEY = "DISPLAY_TEXT_MAP"

        fun newInstance(maps: HashMap<StateViewModel.DisplayType, Int>): StateDisplayFragment {
            return StateDisplayFragment().apply {
                arguments = Bundle().also { it.putSerializable(DISPLAY_TEXT_MAPS_KEY, maps) }
            }
        }
    }
}