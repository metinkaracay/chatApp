package com.example.learnandroidproject.ui.welcome.logInFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.databinding.FragmentLogInBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                Log.e("test","$it")
            }
            token.observe(viewLifecycleOwner){
                Log.e("test3","${requireContext().getPackageName()}")
                val sharedPreferences = requireContext().getSharedPreferences("com.example.learnandroidproject.ui.welcome.logInFragment",Context.MODE_PRIVATE)
                sharedPreferences.edit().putString("accessTokenKey", it).apply()

            }
        }
    }

    fun handleViewOption(){
        binding.loginButton.setOnClickListener {
            val userName = binding.userName.text.toString()
            val password = binding.password.text.toString()

            val result = viewModel.checkFields(userName,password)

            if (result) {
                viewModel.viewModelScope.launch {
                    delay(1000)

                    welcomeViewModel.goToBaseChatRoomsPage()
                    Log.e("test2", "Giriş Başarılı")
                }
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