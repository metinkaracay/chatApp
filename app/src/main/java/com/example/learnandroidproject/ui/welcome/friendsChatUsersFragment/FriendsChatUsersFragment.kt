package com.example.learnandroidproject.ui.welcome.friendsChatUsersFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.databinding.FragmentFriendsChatUsersBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel
import com.example.learnandroidproject.ui.welcome.friendsChatUsersFragment.adapter.FriendsUsersAdapter
import com.example.learnandroidproject.ui.welcome.popUpFragment.PopUpFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FriendsChatUsersFragment : BaseFragment<FragmentFriendsChatUsersBinding>() {

    @Inject
    lateinit var recyclerAdapter: FriendsUsersAdapter

    private val viewModel: FriendsChatUsersViewModel by viewModels()
    private val welcomeViewModel: WelcomeViewModel by activityViewModels()

    override fun getLayoutResId(): Int = R.layout.fragment_friends_chat_users

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val clickedUser = welcomeViewModel.getUserInfo()
        val messageArray = welcomeViewModel.getLastSentMessage()
        if (messageArray != null){
            Log.e("gelennmesaj","$messageArray")
        }
        viewModel.sendingMessage = messageArray!!
        // mesajlaştığımız odadaki son mesajın görüldü durumunu kontrol etmek için
        viewModel.clickedUserForCurrentRoom = clickedUser.uId
        if (welcomeViewModel.getExitChatRoomData()){
            welcomeViewModel.exitToChatRoomFillData(false)
            viewModel.updateSeenInfo(clickedUser.uId)
        }

        recyclerAdapter = FriendsUsersAdapter()
        initResultsItemsRecyclerView()
        with(viewModel){
            friendsChatUsersPageViewStateLiveData.observeNonNull(viewLifecycleOwner){
                Log.e("testObserve","Observe etti = ${it.users.size}")
                with(binding){
                    pageViewState = it
                    executePendingBindings()
                }
                recyclerAdapter.setItems(it.users)
            }
        }
        adapterListeners()
        welcomeViewModel.isFriendsListRecording.observeNonNull(viewLifecycleOwner){
            viewModel.clickedUsersList = welcomeViewModel.getClickedUsersList()

            viewModel.listUpdate(it,requireContext())
        }
        welcomeViewModel.isMEssageSended.observeNonNull(viewLifecycleOwner){
            viewModel.clickedUsersList = welcomeViewModel.getClickedUsersList()
            Log.e("ismessageSended","çalıştı")
            viewModel.listUpdateForSending()
        }
        viewModel.listUpdated.observeNonNull(viewLifecycleOwner){
            if (it){
                Log.e("observelist","çalıştı")
                viewModel.updateSeenStateClickedUser(viewModel.clickedUserForCurrentRoom)
            }
        }
        viewModel.notificationUserFilled.observeNonNull(viewLifecycleOwner){
            Log.e("bildirimdeki boolean","$it")
            if (it){
                var user = viewModel.notificationUser

                if (user != null){
                    Log.e("bildirimdeki user","Id: ${user.uId}, Name: ${user.uName}")
                    welcomeViewModel.fillUserInfoData(user.uId,user.uName,user.uStatu,user.uPhoto)
                    welcomeViewModel.goToChattingPage()
                    viewModel.fixObserver()
                }else{
                    Log.e("bildirimdeki user","boş geldi")
                }
            }
        }

        welcomeViewModel.testSingleLiveEvent.observeNonNull(viewLifecycleOwner){
            Log.e("test_Single_Live_Text","FriendChat gelen : ${it}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onBackground() {
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onForeground() {
                viewModel.getAllUsers()
                Toast.makeText(requireContext(),"İstek atıldı",Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun adapterListeners(){
        recyclerAdapter.setItemClickListener{
            welcomeViewModel.fillUserInfoData(it.uId,it.uName,it.uStatu,it.uPhoto)
            viewModel.updateSeenInfo(it.uId)
            welcomeViewModel.goToChattingPage()
        }
        recyclerAdapter.setPhotoItemClickListener {
            welcomeViewModel.fillClickedUserPhoto(it)
            showUserProfilePhotoDialog(3)
        }
        welcomeViewModel.additionalDataSingleLiveEvent.observeNonNull(viewLifecycleOwner){
            if (it){
                viewModel.findUserForNotification(welcomeViewModel.getAdditionalId().toString())
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
    private fun showUserProfilePhotoDialog(requestCode: Int) {
        if (PopUpFragment.isShowing().not()) {
            val filterNewDialogFragment: PopUpFragment = PopUpFragment.newInstance(requestCode)
            filterNewDialogFragment.show(childFragmentManager, PopUpFragment::class.java.simpleName)
        }
    }
}