package com.example.learnandroidproject.ui.welcome.secondFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.databinding.FragmentSecondBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondFragment : BaseFragment<FragmentSecondBinding>() {

    private val welcomeViewModel: WelcomeViewModel by activityViewModels()
    private val secondViewModel: SecondViewModel by viewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_second

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleViewOptions()
        with(secondViewModel) {
            secondFragmentPageViewStateLiveData.observeNonNull(viewLifecycleOwner) {
                with(binding) {
                    pageViewState = it
                    executePendingBindings()
                }
            }
        }
    }

    private fun handleViewOptions() {
        with(binding) {
            backButton.setOnClickListener {
                welcomeViewModel.navigateUp()
            }
        }
    }
}