package com.example.learnandroidproject.ui.welcome.groupChattingFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupInfo
import com.example.learnandroidproject.databinding.FragmentGroupChattingBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel
import com.example.learnandroidproject.ui.welcome.chattingFragment.SocketHandler
import com.example.learnandroidproject.ui.welcome.groupChattingFragment.adapter.GroupChattingMessagesAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GroupChattingFragment : BaseFragment<FragmentGroupChattingBinding>() {

    @Inject
    lateinit var recyclerAdapter: GroupChattingMessagesAdapter
    private val viewModel: GroupChattingViewModel by viewModels()
    private val welcomeViewModel: WelcomeViewModel by activityViewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_group_chatting

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireContext().getSharedPreferences("LoggedUserID",Context.MODE_PRIVATE)
        val loggedUserId = sharedPreferences.getString("LoggedUserId","")
        welcomeViewModel.groupMessageSingleLiveEvent.observe(viewLifecycleOwner){
            viewModel.fetchMessagesOnSocket(it)
        }
        val group = welcomeViewModel.getGroupInfo()
        viewModel.group = group // Tıklanan grup bilgilerini çeker
        Log.e("gelen grupp","$group")
        recyclerAdapter = GroupChattingMessagesAdapter()
        initResultsItemsRecyclerView()
        handleViewOptions()
        val senderUserMessage = welcomeViewModel.getLastSentMessage()
        viewModel.sendingMessage = senderUserMessage!!
        with(viewModel){
            groupChattingPageViewStateLiveData.observeNonNull(viewLifecycleOwner){
                with(binding){
                    pageViewState = it
                    executePendingBindings()
                }
                recyclerAdapter.setItems(it.messages,loggedUserId!!.toInt())
            }
            newMessageOnTheChatLiveData.observeNonNull(viewLifecycleOwner){
                if (it){
                    binding.recyclerView.scrollToPosition(this.messageList.size - 1)
                }
            }
            viewModel.errorMessageLiveData.observe(viewLifecycleOwner){
                Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.fetchMessages(requireContext())
    }

    fun handleViewOptions(){
        binding.backArrow.setOnClickListener {
            welcomeViewModel.exitToChatRoomFillData(true)
            welcomeViewModel.navigateUp()
        }
        binding.sendButton.setOnClickListener {
            val message = binding.editText.text.toString()
            viewModel.sendMessage(SocketHandler,requireContext(),message)
            binding.editText.text.clear()

            welcomeViewModel.fillTestSingleEvent(message)
        }
        binding.groupInfo.setOnClickListener {
            welcomeViewModel.goToChatInfoFragment()
        }
        viewModel.messageFetchRequestLiveData.observe(viewLifecycleOwner){
            if (it){
                binding.swipeRefreshLayout.setOnRefreshListener {
                    viewModel.fetchMessages(requireContext())
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }else{
                if (!viewModel.isNewChat){
                    binding.swipeRefreshLayout.isEnabled = false
                    Toast.makeText(requireContext(),"Tüm Mesajlar Yüklendi", Toast.LENGTH_SHORT).show()
                }
            }
            welcomeViewModel.membersList = viewModel.members // TODO geçici çözüm
        }
        binding.editText.setOnClickListener {
            binding.recyclerView.scrollToPosition(viewModel.messageList.size - 1 )
        }
        binding.buttonRace.setOnClickListener {
            viewModel.setRaceState(!viewModel.isRaceStart)
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