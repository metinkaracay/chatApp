package com.example.learnandroidproject.ui.welcome.createProfileFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.request.userRequest.User
import com.example.learnandroidproject.databinding.FragmentCreateProfileBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel
import com.example.learnandroidproject.ui.welcome.popUpFragment.PopUpFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateProfileFragment() : BaseFragment<FragmentCreateProfileBinding>() {

    private val viewModel: CreateProfileViewModel by viewModels()
    private val welcomeViewModel: WelcomeViewModel by activityViewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_create_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.errorMessageLiveData.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
        }
        var user = welcomeViewModel.getUser()

        handleViewOptions(user)

    }
    fun handleViewOptions(user: User){
        binding.sendButton.setOnClickListener {
            val user: User = user
            user.firstName = binding.firstName.text.toString()
            user.lastName = binding.lastName.text.toString().trim()
            user.age = binding.age.text.toString()

            val selectedGenderId = binding.radioGroup.checkedRadioButtonId

            // Seçili RadioButton'ın değerine göre cinsiyeti belirle
            user.gender = when (selectedGenderId) {
                R.id.radioMale -> "Erkek"
                R.id.radioFemale -> "Kadın"
                else -> null
            }
            val result = viewModel.checkMessage(user)

            if (result){
                showNewPhotoDialog()
            }
        }
    }

    private fun showNewPhotoDialog() {
        if (PopUpFragment.isShowing().not()) {
            val filterNewDialogFragment: PopUpFragment = PopUpFragment.newInstance()
            filterNewDialogFragment.show(childFragmentManager, PopUpFragment::class.java.simpleName)
        }
    }
}