package com.example.learnandroidproject.ui.welcome.createAccountFragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.databinding.FragmentCreateAccountBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel

class CreateAccountFragment : BaseFragment<FragmentCreateAccountBinding>() {
    private val viewModel: CreateAccountViewModel by viewModels()

    private val welcomeViewModel: WelcomeViewModel by activityViewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_create_account

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleViewOptions()
        with(viewModel){
            createAccountLiveData.observeNonNull(viewLifecycleOwner){errorMessage ->
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun handleViewOptions(){

        binding.sendButton.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val userName = binding.userName.text.toString().trim()

            val result = viewModel.checkFields(userName,email,password)

            if (result){
                welcomeViewModel.fillUserData(userName,email,password)
                welcomeViewModel.goToCreateProfile()
            }
        }
    }
}