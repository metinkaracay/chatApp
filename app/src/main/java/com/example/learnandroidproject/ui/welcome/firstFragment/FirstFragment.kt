package com.example.learnandroidproject.ui.welcome.firstFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.databinding.FragmentFirstBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstFragment : BaseFragment<FragmentFirstBinding>() {

    private val welcomeViewModel: WelcomeViewModel by activityViewModels()
    private val firstFragmentViewModel: FirstFragmentViewModel by viewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_first

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleViewOptions()
        with(firstFragmentViewModel) {
            firstFragmentPageViewStateLiveData.observeNonNull(viewLifecycleOwner) {
                with(binding) {
                    pageViewState = it
                    executePendingBindings()
                }
            }
        }
    }

    private fun handleViewOptions() {
        with(binding) {
            changeFragmentButton.setOnClickListener {
                welcomeViewModel.onSecondFragmentClicked()
            }
        }
    }
}