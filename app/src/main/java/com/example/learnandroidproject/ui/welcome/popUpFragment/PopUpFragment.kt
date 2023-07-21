package com.example.learnandroidproject.ui.welcome.popUpFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.databinding.FragmentPopUpBinding
import com.example.learnandroidproject.ui.base.BaseDialogFragment
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel

class PopUpFragment : BaseDialogFragment<FragmentPopUpBinding>() {

    private val viewModel: PopUpViewModel by viewModels()
    private val welcomeViewModel: WelcomeViewModel by activityViewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_pop_up

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.attributes.windowAnimations = R.style.Animation_Design_BottomSheetDialog
        with(viewModel) {
            popupPageViewStateLiveData.observeNonNull(viewLifecycleOwner) {
                with(binding) {
                    pageViewState = it
                    executePendingBindings()
                }
            }
            popUpCountDownTimer.observeNonNull(viewLifecycleOwner) { remainingTime ->
                if (remainingTime == 0) {
                    dismiss()
                    welcomeViewModel.goToMainPage()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    companion object {
        private var showing = false
        fun isShowing() = showing
        fun newInstance() = PopUpFragment().apply {
            arguments = bundleOf()
        }
    }

}