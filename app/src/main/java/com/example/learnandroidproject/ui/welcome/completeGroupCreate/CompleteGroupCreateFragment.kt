package com.example.learnandroidproject.ui.welcome.completeGroupCreate

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.databinding.FragmentCompleteGroupCreateBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel
import com.example.learnandroidproject.ui.welcome.completeGroupCreate.adapter.GroupMembersAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CompleteGroupCreateFragment : BaseFragment<FragmentCompleteGroupCreateBinding>() {

    @Inject
    lateinit var recyclerAdapter: GroupMembersAdapter

    private val viewModel: CompleteGroupCreateViewModel by viewModels()
    private val welcomeViewModel: WelcomeViewModel by activityViewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_complete_group_create

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val users = welcomeViewModel.getSelectedUsersForGroupChat()
        viewModel.groupMembers = users
        viewModel.fillMembers(users)
        handleViewOption()
        initResultsItemsRecyclerView()
        with(viewModel){
            completeGroupCreatePageViewStateLiveData.observeNonNull(viewLifecycleOwner){
                with(binding){
                    pageViewState = it
                    executePendingBindings()
                }
                recyclerAdapter.setItems(it.members)
            }
            errorMessagesLiveData.observeNonNull(viewLifecycleOwner){
                Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
                binding.completeButton.isEnabled = true // Eğer grup oluşturamazsa tekrar deneyebilmesi için butonu aktif eder
            }
            newGroupCreatedLiveData.observeNonNull(viewLifecycleOwner){
                welcomeViewModel.fillNewGroupListResponse(it)
                welcomeViewModel.goToBaseChatRoomsPage()
            }
        }
        adapterListener()
    }

    fun handleViewOption(){
        binding.backArrow.setOnClickListener {
            welcomeViewModel.navigateUp()
        }
        binding.completeButton.setOnClickListener {
            val groupName = binding.groupName.text.toString()
            val result = viewModel.checkField(groupName)

            if (result){
                viewModel.editDatas(groupName)
            }
            binding.completeButton.isEnabled = false // Birden fazla grup oluşturmasın diye
        }
    }

    fun adapterListener(){
        recyclerAdapter.setItemClickListener {
            var members = viewModel.groupMembers.toMutableList()
            members.remove(it)

            viewModel.groupMembers = members.toList()
            viewModel.fillMembers(members)
        }
    }

    private fun initResultsItemsRecyclerView() {
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.VERTICAL
            }
            adapter = recyclerAdapter
        }
    }
}