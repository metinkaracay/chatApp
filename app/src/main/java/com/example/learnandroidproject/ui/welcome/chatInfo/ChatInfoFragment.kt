package com.example.learnandroidproject.ui.welcome.chatInfo

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.databinding.FragmentChatInfoBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel
import com.example.learnandroidproject.ui.welcome.chatInfo.adapter.MembersAdapter
import com.example.learnandroidproject.ui.welcome.popUpFragment.PopUpFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatInfoFragment : BaseFragment<FragmentChatInfoBinding>() {

    @Inject
    lateinit var recyclerAdapter: MembersAdapter

    private val viewModel: ChatInfoViewModel by viewModels()
    private val welcomeViewModel: WelcomeViewModel by activityViewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_chat_info

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleViewOption()
        val group = welcomeViewModel.getGroupInfo()
        val members = welcomeViewModel.membersList
        initResultsItemsRecyclerView()
        with(viewModel){
            chatInfoPageViewStateLiveData.observeNonNull(viewLifecycleOwner){
                with(binding){
                    pageViewState = it
                    executePendingBindings()
                }
                recyclerAdapter.setItems(it.groupMembers)
            }
        }
        viewModel.fetchGroup(group,members)
    }
    fun handleViewOption(){
        binding.backArrow.setOnClickListener {
            welcomeViewModel.navigateUp()
        }
        binding.userPhoto.setOnClickListener {
            showUserProfilePhotoDialog(3)
        }
    }

    private fun showUserProfilePhotoDialog(requestCode: Int) {
        if (PopUpFragment.isShowing().not()) {
            val filterNewDialogFragment: PopUpFragment = PopUpFragment.newInstance(requestCode)
            filterNewDialogFragment.show(childFragmentManager, PopUpFragment::class.java.simpleName)
        }
    }
    private fun initResultsItemsRecyclerView() {
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.VERTICAL
                stackFromEnd= true
            }
            adapter = recyclerAdapter
        }
    }
}