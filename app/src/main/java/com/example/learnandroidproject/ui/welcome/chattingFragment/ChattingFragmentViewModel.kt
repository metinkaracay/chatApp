package com.example.learnandroidproject.ui.welcome.chattingFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.common.isSuccess
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.SendingMessage
import com.example.learnandroidproject.data.local.model.dating.db.request.userRequest.User
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import com.example.learnandroidproject.ui.welcome.generalChatUsersFragment.GeneralChatUsersPageViewState
import com.github.michaelbull.result.fold
import com.github.michaelbull.result.get
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
@HiltViewModel
class ChattingFragmentViewModel @Inject constructor(private val datingApiRepository: DatingApiRepository): BaseViewModel(){

    private val _chattingPageViewStateLiveData: MutableLiveData<ChattingFragmentPageViewState> = MutableLiveData()
    val chattingPageViewStateLiveData: LiveData<ChattingFragmentPageViewState> = _chattingPageViewStateLiveData

    private val _userLiveData: MutableLiveData<ChattingFragmentPageViewState> = MutableLiveData()
    val userLiveData: LiveData<ChattingFragmentPageViewState> = _userLiveData

    var user: UserInfo = UserInfo(0,"","","null")

    fun getUserInfo(){
        _userLiveData.value = userLiveData.value?.copy(userInfo = user)
    }
    fun startFetchingMessagesPeriodically() {
        viewModelScope.launch {
            while (true) {
                getAllMessages()
                delay(TimeUnit.SECONDS.toMillis(5))
            }
        }
    }
    fun getAllMessages(){
        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.getMessages(user.uId.toString()).get()?.let {
                withContext(Dispatchers.Main){
                    _chattingPageViewStateLiveData.value = ChattingFragmentPageViewState(user,it)
                }
            }
        }
    }
    fun sendMessage(message: String){
        Log.e("mesaj","$message")
        viewModelScope.launch(Dispatchers.IO){
            val result = datingApiRepository.sendMessage(user.uId.toString(),SendingMessage(message))

            if (result.isSuccess()){
                Log.e("postSonuç","mesaj Gönderildi")
            }else{
                Log.e("postSonuç","Hata")
            }
        }
    }
}