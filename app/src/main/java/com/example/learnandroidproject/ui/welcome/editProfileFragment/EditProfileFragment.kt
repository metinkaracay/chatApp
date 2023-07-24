package com.example.learnandroidproject.ui.welcome.editProfileFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.databinding.FragmentEditProfileBinding
import com.example.learnandroidproject.ui.base.BaseFragment

class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>() {

    private val viewModel: EditProfileViewModel by viewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_edit_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel){
            editProfilePageViewStateLiveData.observeNonNull(viewLifecycleOwner){
                with(binding){
                    pageViewState = it
                    executePendingBindings()
                }
            }
        }
    }
}