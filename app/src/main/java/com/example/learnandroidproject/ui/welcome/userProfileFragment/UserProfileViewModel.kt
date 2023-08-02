package com.example.learnandroidproject.ui.welcome.userProfileFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import com.github.michaelbull.result.get
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(private val datingApiRepository: DatingApiRepository): BaseViewModel() {

    private val _userProfilePageViewStateLiveData: MutableLiveData<UserProfilePageViewState> = MutableLiveData()
    val userProfilePageViewStateLiveData: LiveData<UserProfilePageViewState> = _userProfilePageViewStateLiveData

    fun fetchUserData(userId: String){
        Log.e("FetchUserData","$userId")
        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.getUserProfile(userId).get()?.let {
                _userProfilePageViewStateLiveData.value = UserProfilePageViewState(it)
            }
        }
    }
}