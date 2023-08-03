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
        recyclerAdapter = FriendsUsersAdapter()
        initResultsItemsRecyclerView()
        with(viewModel){
            friendsChatUsersPageViewStateLiveData.observeNonNull(viewLifecycleOwner){
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
            Log.e("isFriends","çalıştı")
            viewModel.listUpdate(it,requireContext())
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
            welcomeViewModel.goToChattingPage()
        }
        recyclerAdapter.setPhotoItemClickListener {
            welcomeViewModel.fillClickedUserPhoto(it)
            showUserProfilePhotoDialog(3)
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