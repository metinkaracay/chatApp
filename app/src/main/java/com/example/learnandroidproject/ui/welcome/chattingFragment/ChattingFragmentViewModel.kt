package com.example.learnandroidproject.ui.welcome.chattingFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.data.local.model.dating.db.request.userRequest.User
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
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
class ChattingFragmentViewModel @Inject constructor(private val datingApiRepository: DatingApiRepository): BaseViewModel(){

    private val _chattingPageViewStateLiveData: MutableLiveData<ChattingFragmentPageViewState> = MutableLiveData()
    val chattingPageViewStateLiveData: LiveData<ChattingFragmentPageViewState> = _chattingPageViewStateLiveData

    private val _userLiveData: MutableLiveData<ChattingFragmentPageViewState> = MutableLiveData()
    val userLiveData: LiveData<ChattingFragmentPageViewState> = _userLiveData

    var nullUser: UserInfo = UserInfo(0,"","","null")

    fun getUserInfo(user: UserInfo){
        nullUser = user
        _userLiveData.value = userLiveData.value?.copy(userInfo = user)
    }

    fun getAllMessages(user: UserInfo){
        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.getMessages(user.uId.toString()).get()?.let {
                withContext(Dispatchers.Main){
                    _chattingPageViewStateLiveData.value = ChattingFragmentPageViewState(nullUser,it)
                }
            }
        }
    }
}