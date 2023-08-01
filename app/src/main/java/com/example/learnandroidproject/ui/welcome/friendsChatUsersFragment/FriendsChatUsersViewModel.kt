package com.example.learnandroidproject.ui.welcome.friendsChatUsersFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import com.example.learnandroidproject.ui.welcome.generalChatUsersFragment.GeneralChatUsersPageViewState
import com.github.michaelbull.result.get
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FriendsChatUsersViewModel@Inject constructor(private val datingApiRepository: DatingApiRepository): BaseViewModel() {

    private val _friendsChatUsersPageViewStateLiveData: MutableLiveData<FriendsChatUsersPageViewState> = MutableLiveData()
    val friendsChatUsersPageViewStateLiveData: LiveData<FriendsChatUsersPageViewState> = _friendsChatUsersPageViewStateLiveData

    init {
        getAllUsers()
    }

    fun getAllUsers(){

        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.fetchFriendsUsers().get()?.let {
                withContext(Dispatchers.Main){
                    _friendsChatUsersPageViewStateLiveData.value = FriendsChatUsersPageViewState(it)
                }
            }
        }
    }
}