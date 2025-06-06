package com.example.learnandroidproject.ui.welcome.logInFragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.LoginRequest
import com.example.learnandroidproject.databinding.FragmentLogInBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInFragment : BaseFragment<FragmentLogInBinding>() {

    private val viewModel: LoginFragmentViewModel by viewModels()
    private val welcomeViewModel: WelcomeViewModel by activityViewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_log_in

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleViewOption()
        with(viewModel){
            logInPageViewStateLiveData.observeNonNull(viewLifecycleOwner){
                with(binding){
                    pageViewState = it
                    executePendingBindings()
                }
            }
            errorMessageLiveData.observe(viewLifecycleOwner){
                Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
            }
            loginStateLiveData.observe(viewLifecycleOwner){
                if (it){
                    welcomeViewModel.goToBaseChatRoomsPage()
                }
            }
        }
    }
    fun handleViewOption(){
        binding.loginButton.setOnClickListener {
            val userName = binding.userName.text.toString()
            val password = binding.password.text.toString()

            val result = viewModel.checkFields(userName,password,requireContext())

            if (result) {
                viewModel.postUserParams(LoginRequest(userName,password,null),requireContext())
            }
        }
        binding.signInButton.setOnClickListener {
            welcomeViewModel.goToCreateAccount()
        }
        binding.backArrow.setOnClickListener {
            welcomeViewModel.navigateUp()
        }
    }
}