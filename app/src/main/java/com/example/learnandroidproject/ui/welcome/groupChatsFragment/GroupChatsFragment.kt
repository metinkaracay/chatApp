package com.example.learnandroidproject.ui.welcome.groupChatsFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.databinding.FragmentGroupChatsBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel
import com.example.learnandroidproject.ui.welcome.groupChatsFragment.adapter.GroupsAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class GroupChatsFragment : BaseFragment<FragmentGroupChatsBinding>() {
    @Inject
    lateinit var recyclerAdapter: GroupsAdapter
    private val viewModel: GroupChatsViewModel by viewModels()
    private val welcomeViewModel: WelcomeViewModel by activityViewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_group_chats

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerAdapter = GroupsAdapter()
        initResultsItemsRecyclerView()
        handleViewOptions()
        with(viewModel){
            groupChatsPageViewStateLiveData.observeNonNull(viewLifecycleOwner){
                with(binding){
                    pageViewState = it
                    executePendingBindings()
                }
                recyclerAdapter.setItems(it.groups)
            }
        }
        adapterListener()

    }

    fun handleViewOptions(){

    }

    fun adapterListener(){
        recyclerAdapter.setItemClickListener {
            welcomeViewModel.fillGroupInfoData(it.groupId,it.groupName,it.groupPhoto)
            welcomeViewModel.goToGroupChattingPage()
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