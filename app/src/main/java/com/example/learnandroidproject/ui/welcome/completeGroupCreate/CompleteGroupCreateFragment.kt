package com.example.learnandroidproject.ui.welcome.completeGroupCreate

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
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.databinding.FragmentCompleteGroupCreateBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompleteGroupCreateFragment : BaseFragment<FragmentCompleteGroupCreateBinding>() {

    private val viewModel: CompleteGroupCreateViewModel by viewModels()
    private val welcomeViewModel: WelcomeViewModel by activityViewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_complete_group_create

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("gelen liste","${welcomeViewModel.getSelectedUsersForGroupChat()}")
        handleViewOption()
        with(viewModel){
            completeGroupCreatePageViewStateLiveData.observeNonNull(viewLifecycleOwner){
                with(binding){
                    pageViewState = it
                    executePendingBindings()
                }
            }
            errorMessagesLiveData.observeNonNull(viewLifecycleOwner){
                Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun handleViewOption(){
        binding.backArrow.setOnClickListener {
            welcomeViewModel.navigateUp()
        }
        binding.completeButton.setOnClickListener {
            val groupName = binding.groupName.text.toString()
            val result = viewModel.checkField(groupName)

            if (result){
                viewModel.editDatas(welcomeViewModel.getSelectedUsersForGroupChat(),groupName)
            }

        }
    }
}