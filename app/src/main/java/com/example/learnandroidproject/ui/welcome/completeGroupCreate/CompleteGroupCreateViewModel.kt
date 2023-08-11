package com.example.learnandroidproject.ui.welcome.completeGroupCreate

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.common.isSuccess
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.GroupInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
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

    private var group = GroupInfo("", arrayListOf())

    init {
        _completeGroupCreatePageViewStateLiveData.value = CompleteGroupCreatePageViewState()
    }

    fun createGroup(){
        viewModelScope.launch(Dispatchers.IO){
            val result = datingApiRepository.createGroup(group)
            if (result.isSuccess()){
                _errorMessagesLiveData.postValue("Kayıt Başarılı")
            }else{
                _errorMessagesLiveData.postValue("Bağlantı hatası. Lütfen internetinizi kontrol edin")
            }
        }
    }

    fun editDatas(selectedUsers: List<UserInfo>, groupName: String){
        var userIds = arrayListOf<Int>()
        for (i in 0 until selectedUsers.size){
            userIds.add(selectedUsers[i].uId)
        }
        group = GroupInfo(groupName,userIds)
        createGroup()
    }

    fun checkField(groupName: String): Boolean{
        if (groupName.isNullOrEmpty()){
            _errorMessagesLiveData.postValue("Grup ismi boş bırakılamaz")
            return false
        }
        return true
    }
}