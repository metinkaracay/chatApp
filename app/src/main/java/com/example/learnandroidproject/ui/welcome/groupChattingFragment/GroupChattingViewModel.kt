package com.example.learnandroidproject.ui.welcome.groupChattingFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import com.github.michaelbull.result.get
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GroupChattingViewModel @Inject constructor(private val datingApiRepository: DatingApiRepository) : BaseViewModel() {

    private val _groupChattingPageViewStateLiveData: MutableLiveData<GroupChattingPageViewState> = MutableLiveData()
    val groupChattingPageViewStateLiveData: LiveData<GroupChattingPageViewState> = _groupChattingPageViewStateLiveData

    var messageList: List<MessageItem> = arrayListOf()
    var group: GroupInfo = GroupInfo(0,"","","null","null",false) // TODO seeni düzeltmen gerekebilir
    private var pageId = 1

    init {
        fetchMessages()
    }

    fun fetchMessages(){
        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.getGroupMessagesFromPage(group.groupId.toString(),pageId).get()?.let {
                withContext(Dispatchers.Main){
                    _groupChattingPageViewStateLiveData.value = GroupChattingPageViewState(group,it)
                    messageList = it + messageList
                    pageId++
                }
            }
        }
    }
}