package com.example.learnandroidproject.ui.welcome.chattingFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.Args
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem
import com.example.learnandroidproject.databinding.FragmentChattingBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel
import com.example.learnandroidproject.ui.welcome.chattingFragment.adapter.ChattingMessagesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class ChattingFragment : BaseFragment<FragmentChattingBinding>() {

    private val viewModel: ChattingFragmentViewModel by viewModels()
    private val welcomeViewModel: WelcomeViewModel by activityViewModels()

    @Inject
    lateinit var chattingMessagesAdapter: ChattingMessagesAdapter
    override fun getLayoutResId(): Int = R.layout.fragment_chatting
    var pageId = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        welcomeViewModel.messageSingleLiveEvent.observe(viewLifecycleOwner){
            viewModel.fetchMessagesOnSocket(it)
        }
        val user = welcomeViewModel.getUserInfo()
        viewModel.user = user
        viewModel.getUserInfo()

        val senderUserMessage = welcomeViewModel.getLastSentMessage()
        if (senderUserMessage != null){
            Log.e("gelennmesaj","$senderUserMessage")
        }
        viewModel.sendingMessage = senderUserMessage!!
        handleViewOption()
        initResultsItemsRecyclerView()
        with(viewModel){
            chattingPageViewStateLiveData.observeNonNull(viewLifecycleOwner){
                with(binding){
                    pageViewState = it
                    executePendingBindings()
                }
                chattingMessagesAdapter.setItems(it.messages,it.userInfo.uId,it.firstMessage)
                scrollToBottom(it.messages)
            }
        }
        viewModel.errorMessageLiveData.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
        }
        // Telefonun navigation bar'ında ki geri tuşuna basılmasını kontrol eder
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    fun handleViewOption(){
        binding.backArrow.setOnClickListener {
            Log.e("çıkıştaki model","${viewModel.sendingMessage}")
            var messageData =viewModel.sendingMessage
            welcomeViewModel.fillLastSentMessage(messageData)
            welcomeViewModel.exitToChatRoomFillData(true)
            welcomeViewModel.navigateUp()
        }
        binding.sendButton.setOnClickListener {
            val message = binding.editText.text.toString()
            viewModel.sendMessage(SocketHandler,requireContext(),message)
            //var messageData = viewModel.sendMessage(SocketHandler,requireContext(),message)
            //welcomeViewModel.fillLastSentMessage(messageData)
            binding.editText.text.clear()
        }
        binding.userInfo.setOnClickListener{
            welcomeViewModel.goToUserProfileFragment()
        }
        viewModel.messageFetchRequestLiveData.observe(viewLifecycleOwner){
            if (it){
                binding.swipeRefreshLayout.setOnRefreshListener {
                    pageId++
                    viewModel.getMessagesFromPage(pageId)
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }else{
                if (!viewModel.isNewChat){
                    Toast.makeText(requireContext(),"Tüm Mesajlar Yüklendi",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun scrollToBottom(list: List<MessageItem>) {
        binding.recyclerView?.let {
            val itemCount = list.size
            if (itemCount > 0) {
                it.scrollToPosition(itemCount - 1)
            }
        }
        // UI'a erişebilmesi için Main thread kullandık
        /*CoroutineScope(Dispatchers.Main).launch {
            delay(TimeUnit.MILLISECONDS.toMillis(100))
            binding.recyclerView?.let {
                val itemCount = list.size
                if (itemCount > 0) {
                    it.scrollToPosition(itemCount - 1)
                }
            }
        }*/
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