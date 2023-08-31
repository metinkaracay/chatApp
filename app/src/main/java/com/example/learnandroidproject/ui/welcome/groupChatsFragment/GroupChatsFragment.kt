package com.example.learnandroidproject.ui.welcome.groupChatsFragment

import android.os.Bundle
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
        val clickedGroup = welcomeViewModel.getGroupInfo() // Son tıklanan grup
        if (welcomeViewModel.getExitChatRoomData()){
            welcomeViewModel.exitToChatRoomFillData(false)
            viewModel.updateSeenStateClickedUser(clickedGroup.groupId)
        }
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
            listUpdated.observeNonNull(viewLifecycleOwner){
                if (it){
                    // mesajlaştığımız odadaki son mesajın görüldü durumunu kontrol etmek için
                    viewModel.updateSeenStateClickedUser(clickedGroup.groupId)
                }
            }
        }
        welcomeViewModel.isGroupListRecording.observeNonNull(viewLifecycleOwner){
            viewModel.listUpdate(it,requireContext())
        }
        welcomeViewModel.isNewGroupCreated.observeNonNull(viewLifecycleOwner){
            if (it){
                viewModel.fetchGroupListWithNewGroups(welcomeViewModel.getNewGroupListResponse())
            }
        }
        welcomeViewModel.groupEventRecording.observeNonNull(viewLifecycleOwner){
            viewModel.refreshEventState(it)
        }
        adapterListener()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onBackground() {
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onForeground() {
                viewModel.getAllGroups()
                Toast.makeText(requireContext(),"İstek atıldı", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun handleViewOptions(){
    }

    fun adapterListener(){
        recyclerAdapter.setItemClickListener {
            welcomeViewModel.fillGroupInfoData(it.groupId,it.groupName,it.groupPhoto,it.isEvent)
            viewModel.updateSeenInfo(it.groupId)
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