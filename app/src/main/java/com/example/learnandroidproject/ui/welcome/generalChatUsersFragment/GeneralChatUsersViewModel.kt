package com.example.learnandroidproject.ui.welcome.generalChatUsersFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.common.GenericResult
import com.example.learnandroidproject.data.local.model.dating.db.request.userRequest.User
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import com.github.michaelbull.result.get
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GeneralChatUsersViewModel @Inject constructor(private val datingApiRepository: DatingApiRepository): BaseViewModel() {

    private val _generalChatUsersPageViewStateLiveData: MutableLiveData<GeneralChatUsersPageViewState> = MutableLiveData()
    val generalChatUsersPageViewStateLiveData: LiveData<GeneralChatUsersPageViewState> = _generalChatUsersPageViewStateLiveData

    init {
        getAllUsers()
    }

    fun getAllUsers(){

        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.fetchUsersForChatRooms().get()?.let {
                withContext(Dispatchers.Main){
                    _generalChatUsersPageViewStateLiveData.value = GeneralChatUsersPageViewState(it)
                }
                Log.e("userfet","${it[0].uPhoto}")
            }
        }
    }
}