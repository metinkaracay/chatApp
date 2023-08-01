package com.example.learnandroidproject.ui.welcome

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.SingleLiveEvent
import com.example.learnandroidproject.data.local.model.dating.db.request.userRequest.User
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.ui.common.navigation.NavigationData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor() : ViewModel() {

    private val _navigateToDestinationSingleLiveEvent: SingleLiveEvent<NavigationData> = SingleLiveEvent()
    private val _navigateUpSingleLiveEvent: SingleLiveEvent<Any?> = SingleLiveEvent()
    private val _closePageSingleLiveEvent: SingleLiveEvent<Any?> = SingleLiveEvent() //TODO Mutable single event ile istek at sockette

    val closePageSingleLiveEvent: LiveData<Any?> = _closePageSingleLiveEvent
    val navigateToDestinationSingleLiveEvent: LiveData<NavigationData> = _navigateToDestinationSingleLiveEvent
    val navigateUpSingleLiveEvent: LiveData<Any?> = _navigateUpSingleLiveEvent

    private var user: User = User(null, null, null, null, null, null, null,null,null)
    private var userInfo = UserInfo(0,"null","null","null",null,null)
    private var clickedUserPhoto: String? = null

    fun fillUserData(userName: String, email: String, password: String) {
        user.userName = userName
        user.email = email
        user.password = password
    }
    fun fillUserInfoData(id: Int,name: String, statu: String, photo: String) {
        userInfo.uId = id
        userInfo.uName = name
        userInfo.uStatu = statu
        userInfo.uPhoto = photo
    }
    fun getUserInfo(): UserInfo {
        return userInfo
    }
    fun getUser(): User {
        return user
    }
    fun fillClickedUserPhoto(photo: String){
        clickedUserPhoto = photo
    }
    fun getClickedUserPhoto(): String? {
        return clickedUserPhoto
    }

    fun onSecondFragmentClicked() {
        _navigateToDestinationSingleLiveEvent.value = NavigationData(destinationId = R.id.secondFragment)
    }

    fun goToMainPage(){
        _navigateToDestinationSingleLiveEvent.value = NavigationData(destinationId = R.id.mainFragment)
    }
    fun goToCreateAccount(){
        _navigateToDestinationSingleLiveEvent.value = NavigationData(destinationId = R.id.createAccountFragment)
    }
    fun goToCreateProfile(){
        _navigateToDestinationSingleLiveEvent.value = NavigationData(destinationId = R.id.createProfileFragment)
    }
    fun goToLoginPage(){
        _navigateToDestinationSingleLiveEvent.value = NavigationData(destinationId = R.id.logInFragment)
    }
    fun goToBaseChatRoomsPage(){
        _navigateToDestinationSingleLiveEvent.value = NavigationData(destinationId = R.id.baseChatRoomsFragment)
    }
    fun goToProfilePage(){
        _navigateToDestinationSingleLiveEvent.value = NavigationData(destinationId = R.id.editProfileFragment)
    }
    fun goToChattingPage(){
        _navigateToDestinationSingleLiveEvent.value = NavigationData(destinationId = R.id.chattingFragment)
    }
    fun goToGenerelChatUsersFragment(){
        _navigateToDestinationSingleLiveEvent.value = NavigationData(destinationId = R.id.generalChatUsersFragment)
    }

    fun navigateUp() {
        _navigateUpSingleLiveEvent.call()
    }
}