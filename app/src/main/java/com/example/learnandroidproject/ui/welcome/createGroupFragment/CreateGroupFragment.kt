package com.example.learnandroidproject.ui.welcome.createGroupFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.databinding.FragmentCreateGroupBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel
import com.example.learnandroidproject.ui.welcome.createGroupFragment.adapter.CreateGroupUsersAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateGroupFragment : BaseFragment<FragmentCreateGroupBinding>() {

    @Inject
    lateinit var recyclerAdapter: CreateGroupUsersAdapter
    private val viewModel: CreateGroupViewModel by viewModels()
    private val welcomeViewModel: WelcomeViewModel by activityViewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_create_group

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerAdapter = CreateGroupUsersAdapter()
        handleViewOption()
        initResultsItemsRecyclerView()
        with(viewModel){
            createGroupPageViewStateLiveData.observeNonNull(viewLifecycleOwner){
                with(binding){
                    pageViewState = it
                    executePendingBindings()
                }
                recyclerAdapter.setItems(it.users)
            }
        }
    }

    fun handleViewOption(){
        binding.backArrow.setOnClickListener {
            welcomeViewModel.navigateUp()
        }
        binding.nextButton.setOnClickListener {
            if (recyclerAdapter.getSelectedUserList().size > 1){
                welcomeViewModel.fillSelectedUsersForGroupChat(recyclerAdapter.getSelectedUserList())
                welcomeViewModel.goToCompleteGroupFragment()
            }else{
                Toast.makeText(requireContext(),"En az 2 kişi seçmelisiz",Toast.LENGTH_SHORT).show()
            }
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