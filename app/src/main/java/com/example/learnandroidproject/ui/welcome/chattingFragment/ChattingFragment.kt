package com.example.learnandroidproject.ui.welcome.chattingFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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
        val sharedPreferences = requireContext().getSharedPreferences("LoggedUserID", Context.MODE_PRIVATE)
        val loggedUserId = sharedPreferences.getString("LoggedUserId","")
        welcomeViewModel.messageSingleLiveEvent.observe(viewLifecycleOwner){
            viewModel.fetchMessagesOnSocket(it)
        }
        val user = welcomeViewModel.getUserInfo()
        viewModel.user = user
        viewModel.getUserInfo()
        val senderUserMessage = welcomeViewModel.getLastSentMessage()
        viewModel.sendingMessage = senderUserMessage!! // TODO burada grupla alakalı bir fark var
        handleViewOption()
        initResultsItemsRecyclerView()
        with(viewModel){
            chattingPageViewStateLiveData.observeNonNull(viewLifecycleOwner){
                with(binding){
                    pageViewState = it
                    executePendingBindings()
                }
                chattingMessagesAdapter.setItems(it.messages,loggedUserId!!.toInt())
            }
            newMessageOnTheChatLiveData.observeNonNull(viewLifecycleOwner){
                if (it){
                    binding.recyclerView.scrollToPosition(this.messageList.size - 1)
                }
            }
        }
        viewModel.errorMessageLiveData.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
        }
        // Telefonun navigation bar'ında ki geri tuşuna basılmasını kontrol eder
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                welcomeViewModel.exitToChatRoomFillData(true)
                welcomeViewModel.navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    fun handleViewOption(){
        binding.backArrow.setOnClickListener {
            Log.e("çıkıştaki model","${viewModel.sendingMessage}")
            var messageData = viewModel.sendingMessage // Chat boyunca attığımız mesajları toplayıp chatten çıkıldığı anda topluca gönderiyoruz
            welcomeViewModel.fillLastSentMessage(messageData)
            welcomeViewModel.exitToChatRoomFillData(true)
            welcomeViewModel.navigateUp()
        }
        binding.sendButton.setOnClickListener {
            val message = binding.editText.text.toString()
            viewModel.sendMessage(SocketHandler,requireContext(),message,"text")
            binding.editText.text.clear()

            welcomeViewModel.fillTestSingleEvent(message)
        }
        viewModel.messageFetchRequestLiveData.observe(viewLifecycleOwner){
            if (it){
                binding.swipeRefreshLayout.setOnRefreshListener {
                    viewModel.getMessagesFromPage()
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }else{
                if (!viewModel.isNewChat){
                    binding.swipeRefreshLayout.isEnabled = false
                    Toast.makeText(requireContext(),"Tüm Mesajlar Yüklendi",Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.editText.setOnClickListener {
            binding.recyclerView.scrollToPosition(viewModel.messageList.size - 1 )
        }
    }

    private fun initResultsItemsRecyclerView() {
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.VERTICAL
                stackFromEnd= true
            }
            adapter = chattingMessagesAdapter
        }
    }
}