package com.example.learnandroidproject.ui.welcome.chatInfo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupMember
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import com.github.michaelbull.result.get
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatInfoViewModel @Inject constructor(private val datingApiRepository: DatingApiRepository): BaseViewModel(){

    private val _chatInfoPageViewStateLiveData: MutableLiveData<ChatInfoPageViewState> = MutableLiveData()
    val chatInfoPageViewStateLiveData: LiveData<ChatInfoPageViewState> = _chatInfoPageViewStateLiveData

    /*fun fetchUserData(userId: String){
        Log.e("FetchUserData","$userId")
        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.getUserProfile(userId).get()?.let {
                withContext(Dispatchers.Main){
                    _chatInfoPageViewStateLiveData.value = ChatInfoPageViewState(it)
                }
            }
        }
    }*/

    fun fetchGroup(group: GroupInfo, members: List<GroupMember>){
        _chatInfoPageViewStateLiveData.value = ChatInfoPageViewState(group,members)
    }
}