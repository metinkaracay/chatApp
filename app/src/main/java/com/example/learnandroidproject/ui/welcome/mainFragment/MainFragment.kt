package com.example.learnandroidproject.ui.welcome.mainFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.databinding.FragmentMainBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel

class MainFragment : BaseFragment<FragmentMainBinding>() {

    private val welcomeViewModel: WelcomeViewModel by activityViewModels()
    private val viewModel: MainFragmentViewModel by viewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleViewOptions()
        with(viewModel){
            mainFragmentPageViewStateLiveData.observeNonNull(viewLifecycleOwner){
                with(binding){
                    pageViewState = it
                    executePendingBindings()
                }
            }
        }
    }

    fun handleViewOptions(){
        binding.signInButton.setOnClickListener {
            welcomeViewModel.goToCreateAccount()
        }

        binding.logInButton.setOnClickListener {
            welcomeViewModel.goToLoginPage()
        }
    }
}