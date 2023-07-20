package com.example.learnandroidproject.ui.welcome.createProfileFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.learnandroidproject.R
import com.example.learnandroidproject.databinding.FragmentCreateProfileBinding
import com.example.learnandroidproject.ui.base.BaseFragment

class CreateProfileFragment() : BaseFragment<FragmentCreateProfileBinding>() {

    private val viewModel: CreateProfileViewModel by viewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_create_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleViewOptions()

        viewModel.errorMessageLiveData.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
        }

    }
    fun handleViewOptions(){
        binding.sendButton.setOnClickListener {
            val userName = arguments?.get("userName").toString()
            val email = arguments?.get("email").toString()
            val password = arguments?.get("password").toString()
            val firstName = binding.firstName.text.toString()
            val lastName = binding.lastName.text.toString().trim()

            val selectedGenderId = binding.radioGroup.checkedRadioButtonId

            // Seçili RadioButton'ın değerine göre cinsiyeti belirle
            val gender: String? = when (selectedGenderId) {
                R.id.radioMale -> "Erkek"
                R.id.radioFemale -> "Kadın"
                else -> null
            }

            //viewModel.postUser(userName, email,password,firstName,lastName,)
            viewModel.checkMessage(userName, email,password,firstName,lastName)
        }
    }
}