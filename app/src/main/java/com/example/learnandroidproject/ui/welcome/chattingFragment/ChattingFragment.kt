package com.example.learnandroidproject.ui.welcome.chattingFragment

import android.os.Bundle
import android.util.Log
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
import com.example.learnandroidproject.databinding.FragmentChattingBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel
import com.example.learnandroidproject.ui.welcome.chattingFragment.adapter.ChattingMessagesAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChattingFragment : BaseFragment<FragmentChattingBinding>() {

    private val viewModel: ChattingFragmentViewModel by viewModels()
    private val welcomeViewModel: WelcomeViewModel by activityViewModels()

    @Inject
    lateinit var chattingMessagesAdapter: ChattingMessagesAdapter
    override fun getLayoutResId(): Int = R.layout.fragment_chatting

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleViewOption()
        val user = welcomeViewModel.getUserInfo()
        viewModel.getUserInfo(user)
        initResultsItemsRecyclerView()
        with(viewModel){
            chattingPageViewStateLiveData.observeNonNull(viewLifecycleOwner){
                with(binding){
                    pageViewState = it
                    executePendingBindings()
                }
                chattingMessagesAdapter.setItems(it.messages,it.userInfo.uId)
                Log.e("fragmentit","${it.messages}")
            }
        }
        viewModel.getAllMessages(user)
    }

    fun handleViewOption(){
        binding.backArrow.setOnClickListener {
            welcomeViewModel.navigateUp()
        }
    }

    private fun initResultsItemsRecyclerView() {
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.VERTICAL
            }
            adapter = chattingMessagesAdapter
        }
    }

}