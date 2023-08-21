package com.example.learnandroidproject.ui.welcome.createProfileFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.data.local.model.dating.db.request.userRequest.User
import com.example.learnandroidproject.databinding.FragmentCreateProfileBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel
import com.example.learnandroidproject.ui.welcome.popUpFragment.PopUpFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            user.firstName = binding.firstName.text.toString().trim()
            user.lastName = binding.lastName.text.toString().trim()
            user.age = binding.age.text.toString()

            val selectedGenderId = binding.radioGroup.checkedRadioButtonId

            // Seçili RadioButton'ın değerine göre cinsiyeti belirle
            user.gender = when (selectedGenderId) {
                R.id.radioMale -> "Erkek"
                R.id.radioFemale -> "Kadın"
                else -> null
            }
            val result = viewModel.checkFields(user)
            if (result){
                viewModel.postUser(user,requireContext())

                viewModel.registerStateLiveData.observeNonNull(viewLifecycleOwner){
                    if (it){
                        showRegisterSuccessDialog()
                        viewModel.viewModelScope.launch(Dispatchers.IO){
                            delay(2000L)
                            withContext(Dispatchers.Main){
                                viewModel.loginChatRooms(user,requireContext())
                            }
                        }
                    }
                }
            }
            viewModel.loginStateLiveData.observe(viewLifecycleOwner){
                if (it){
                    welcomeViewModel.goToBaseChatRoomsPage()
                }
            }
        }
    }

    private fun showRegisterSuccessDialog() {
        if (PopUpFragment.isShowing().not()) {
            val filterNewDialogFragment: PopUpFragment = PopUpFragment.newInstance(1)
            filterNewDialogFragment.show(childFragmentManager, PopUpFragment::class.java.simpleName)
        }
    }
}