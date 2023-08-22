package com.example.learnandroidproject.ui.welcome.completeGroupCreate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.GroupData
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupInfo
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import com.github.michaelbull.result.get
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompleteGroupCreateViewModel @Inject constructor(private val datingApiRepository: DatingApiRepository) : BaseViewModel() {

    private val _completeGroupCreatePageViewStateLiveData: MutableLiveData<CompleteGroupCreatePageViewState> = MutableLiveData()
    val completeGroupCreatePageViewStateLiveData: LiveData<CompleteGroupCreatePageViewState> = _completeGroupCreatePageViewStateLiveData

    private val _errorMessagesLiveData: MutableLiveData<String> = MutableLiveData()
    val errorMessagesLiveData: LiveData<String> = _errorMessagesLiveData

    private val _newGroupCreatedLiveData: MutableLiveData<List<GroupInfo>> = MutableLiveData()
    val newGroupCreatedLiveData: LiveData<List<GroupInfo>> = _newGroupCreatedLiveData

    var groupMembers: List<UserInfo> = arrayListOf()
    private var group = GroupData("", arrayListOf())
    var newGroupList: List<GroupInfo> = arrayListOf()

    fun fillMembers(list: List<UserInfo>){
        _completeGroupCreatePageViewStateLiveData.value = CompleteGroupCreatePageViewState(list)
    }

    fun createGroup(){
        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.createGroup(group).get()?.let {
                viewModelScope.launch(Dispatchers.Main){
                    newGroupList = it
                    _newGroupCreatedLiveData.postValue(it)
                }
            }
        }
    }

    fun editDatas(groupName: String){
        var userIds = arrayListOf<Int>()
        for (i in 0 until groupMembers.size){
            userIds.add(groupMembers[i].uId)
        }
        group = GroupData(groupName,userIds)
        if (groupMembers.size >= 2){
            createGroup()
        }else{
            _errorMessagesLiveData.postValue("En az 2 kişi seçmelisiniz")
        }
    }

    fun checkField(groupName: String): Boolean{
        if (groupName.isNullOrEmpty()){
            _errorMessagesLiveData.postValue("Grup ismi boş bırakılamaz")
            return false
        }else if (groupName.length > 20){
            _errorMessagesLiveData.postValue("Daha kısa bir grup ismi seçiniz")
        }
        return true
    }
}