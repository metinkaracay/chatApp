package com.example.learnandroidproject.ui.welcome.editProfileFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.UpdateUser
import com.example.learnandroidproject.databinding.FragmentEditProfileBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel
import com.example.learnandroidproject.ui.welcome.popUpFragment.PopUpFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>() {

    private val viewModel: EditProfileViewModel by viewModels()
    private val welcomeViewModel: WelcomeViewModel by activityViewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_edit_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleViewOption()
        fieldsController(false)
        with(viewModel){
            editProfilePageViewStateLiveData.observeNonNull(viewLifecycleOwner){
                with(binding){
                    pageViewState = it
                    executePendingBindings()
                }
            }
            errorMessageLiveData.observeNonNull(viewLifecycleOwner){
                Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
            }
            userPhotoLiveData.observe(viewLifecycleOwner){
                welcomeViewModel.fillClickedUserPhoto(it)
            }
        }
    }

    fun handleViewOption(){
        binding.saveButton.isEnabled = false

        binding.backArrow.setOnClickListener {
            welcomeViewModel.navigateUp()
        }
        binding.userPhoto.setOnClickListener {
            showNewPhotoPickerDialog(2)
        }
        binding.editButton.setOnClickListener {
            viewModel.editButtonListener(true)
            fieldsController(true)
            binding.saveButton.isEnabled = true
            binding.editButton.isEnabled = false
        }
        binding.saveButton.setOnClickListener {
            binding.editButton.isEnabled = true
            val statu = binding.status.text.toString()
            val firstName = binding.firstName.text.toString().trim()
            val lastName = binding.lastName.text.toString().trim()
            val age = binding.age.text.toString()
            val user = UpdateUser(statu,firstName,lastName,age)

            val result = viewModel.checkFields(user)

            if (result){
                viewModel.patchChangedUserFields(user)
                viewModel.editButtonListener(false)
                fieldsController(false)
            }
        }
    }

    fun fieldsController(state: Boolean){
        binding.status.isEnabled = state
        binding.firstName.isEnabled = state
        binding.lastName.isEnabled = state
        binding.age.isEnabled = state
    }
    private fun showNewPhotoPickerDialog(requestCode: Int) {
        if (PopUpFragment.isShowing().not()) {
            val filterNewDialogFragment: PopUpFragment = PopUpFragment.newInstance(requestCode)
            filterNewDialogFragment.setCallBackListener{
                viewModel.updateToPhoto(it)
            }
            filterNewDialogFragment.show(childFragmentManager, PopUpFragment::class.java.simpleName)
        }
    }
}