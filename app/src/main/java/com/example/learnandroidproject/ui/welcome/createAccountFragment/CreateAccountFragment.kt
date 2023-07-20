package com.example.learnandroidproject.ui.welcome.createAccountFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.learnandroidproject.R
import com.example.learnandroidproject.databinding.FragmentCreateAccountBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel

class CreateAccountFragment : BaseFragment<FragmentCreateAccountBinding>() {

    private val welcomeViewModel: WelcomeViewModel by activityViewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_create_account

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleViewOptions()

    }

    fun handleViewOptions(){

        binding.sendButton.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val userName = binding.userName.text.toString().trim()
            // TODO email format kontrolü yapılmalı


            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Tüm bilgiler tamam, işleme devam edebilir
                Log.e("SignInFragment", "Email: $email, Şifre: $password")
                welcomeViewModel.goToCreateProfile(userName,email,password)

            } else {
                // Seçim yapmadığı için kullanıcıya uyarı göster
                Toast.makeText(requireContext(), "Lütfen Tüm Alanları Doldurun", Toast.LENGTH_SHORT).show()
            }
        }
    }
}