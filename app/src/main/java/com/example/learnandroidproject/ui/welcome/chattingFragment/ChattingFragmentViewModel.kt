package com.example.learnandroidproject.ui.welcome.chattingFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.ui.base.BaseViewModel

class ChattingFragmentViewModel : BaseViewModel(){

    private val _chattingPageViewStateLiveData: MutableLiveData<ChattingFragmentPageViewState> = MutableLiveData()
    val chattingPageViewStateLiveData: LiveData<ChattingFragmentPageViewState> = _chattingPageViewStateLiveData

    fun getUserInfo(user: UserInfo){
        Log.e("userName","${user.uName}")
        _chattingPageViewStateLiveData.value = ChattingFragmentPageViewState(user)
    }


}