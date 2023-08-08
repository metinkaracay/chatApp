package com.example.learnandroidproject.ui.welcome.friendsChatUsersFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.databinding.FragmentFriendsChatUsersBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel
import com.example.learnandroidproject.ui.welcome.chattingFragment.SocketHandler
import com.example.learnandroidproject.ui.welcome.friendsChatUsersFragment.adapter.FriendsUsersAdapter
import com.example.learnandroidproject.ui.welcome.generalChatUsersFragment.adapter.GeneralUsersAdapter
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
        //
        viewModel.clickedUserForCurrentRoom = clickedUser.uId
        if (welcomeViewModel.getExitChatRoomData()){
            welcomeViewModel.exitToChatRoomFillData(false)
            viewModel.clickedUserId = clickedUser.uId //TODO burayı iyileştir
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
        welcomeViewModel.messageMutableLiveEvent.observeNonNull(viewLifecycleOwner){
            Log.e("sonmesaj","${it.message}")
            Log.e("sonmesaj2","${it.receiverId}")
            //viewModel.sortFriendList(it,requireContext())
        }
        welcomeViewModel.isFriendsListRecording.observeNonNull(viewLifecycleOwner){
            viewModel.clickedUsersList = welcomeViewModel.getClickedUsersList()
            viewModel.listUpdate(it,requireContext())
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
    }
    /*override fun onStart() {
        super.onStart()
        val data = welcomeViewModel.userMessages
        Log.e("isFriendsGelen","${data}")
        viewModel.listUpdate(welcomeViewModel.userMessages,requireContext())
    }*/
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