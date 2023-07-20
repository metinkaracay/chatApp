package com.example.learnandroidproject.ui.welcome.testFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.databinding.FragmentTestBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestFragment : BaseFragment<FragmentTestBinding>() {

    private val welcomeViewModel: WelcomeViewModel by viewModels()

    private val viewModel: TestViewModel by viewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_test

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel){
            testPageViewStateLiveData.observeNonNull(viewLifecycleOwner){
                with(binding) {
                    pageViewState = it
                    executePendingBindings()
                }
            }
        }
    }
}