package com.example.learnandroidproject.ui.welcome.editProfileFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.UpdateUser
import com.example.learnandroidproject.databinding.FragmentEditProfileBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.popUpFragment.PopUpFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>() {

    private val viewModel: EditProfileViewModel by viewModels()

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
        }
    }

    fun handleViewOption(){

        binding.userPhoto.setOnClickListener {
            showNewPhotoPickerDialog(2)
        }
        binding.editButton.setOnClickListener {
            viewModel.editButtonListener(true)
            fieldsController(true)
        }
        binding.saveButton.setOnClickListener {
            val statu = binding.status.text.toString()
            val firstName = binding.firstName.text.toString()
            val lastName = binding.lastName.text.toString()
            val age = binding.age.text.toString()
            val user = UpdateUser(statu,firstName,lastName,age)

            val result = viewModel.checkFields(user)

            if (result){
                viewModel.patchChangedUserFields(user)
                viewModel.editButtonListener(false)
            }
            fieldsController(false)
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