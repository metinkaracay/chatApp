package com.example.learnandroidproject.ui.welcome

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.SingleLiveEvent
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.Args
import com.example.learnandroidproject.data.local.model.dating.db.request.userRequest.User
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem
import com.example.learnandroidproject.ui.common.navigation.NavigationData
import com.example.learnandroidproject.ui.welcome.chattingFragment.SocketHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor() : ViewModel() {

    private val _navigateToDestinationSingleLiveEvent: SingleLiveEvent<NavigationData> = SingleLiveEvent()
    private val _navigateUpSingleLiveEvent: SingleLiveEvent<Any?> = SingleLiveEvent()
    private val _closePageSingleLiveEvent: SingleLiveEvent<Any?> = SingleLiveEvent()
    private val _messageMutableLiveEvent: MutableLiveData<Args> = MutableLiveData()
    private val _isFriendsListRecording: MutableLiveData<MutableMap<String, MutableList<Args>>> = MutableLiveData()

    val closePageSingleLiveEvent: LiveData<Any?> = _closePageSingleLiveEvent
    val navigateToDestinationSingleLiveEvent: LiveData<NavigationData> = _navigateToDestinationSingleLiveEvent
    val navigateUpSingleLiveEvent: LiveData<Any?> = _navigateUpSingleLiveEvent
    val messageMutableLiveEvent: MutableLiveData<Args> = _messageMutableLiveEvent
    val isFriendsListRecording: MutableLiveData<MutableMap<String, MutableList<Args>>> = _isFriendsListRecording

    private var user: User = User(null, null, null, null, null, null, null,null,null)
    private var userInfo = UserInfo(0,"null","null","null",null,null)
    private var clickedUserPhoto: String? = null
    val userMessages: MutableMap<String, MutableList<Args>> = mutableMapOf()


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
    fun socketListener(Socket: SocketHandler, context: Context){
        val sharedPreferences = context.getSharedPreferences("LoggedUserID",Context.MODE_PRIVATE)
        val loggedUserId = sharedPreferences.getString("LoggedUserId","")

        Socket.setSocket(context)
        Socket.establishConnection()
        val mSocket = Socket.getSocket()

        mSocket.on("message"){ args ->
            if (args[0] != null){
                var argsModel = Args("","","","")

                Log.e("welalıcı","${args[1]}")
                Log.e("welsende","${args[0]}")
                if(args[0].toString() == loggedUserId){
                    argsModel = Args(args[1].toString(),args[0].toString(),args[3].toString(),args[2].toString())
                    Log.e("weltest","çalıştı")
                }else{
                    argsModel = Args(args[1].toString(),args[0].toString(),loggedUserId.toString(),args[2].toString())
                }

                val senderId = argsModel.senderId
                val receiverId = argsModel.receiverId//if (senderId == loggedUserId) args[2].toString() else senderId
                val messageContent = argsModel.message
                val messageDate = argsModel.messageTime

                val newArgsModel = Args(messageContent, senderId, receiverId, messageDate)

                // Mesajları kullanıcıya göre gruplayarak saklayalım.
                if (userMessages.containsKey(receiverId)) {
                    Log.e("socketListener","ifedüştü")
                    userMessages[receiverId]?.add(newArgsModel)
                } else {
                    Log.e("socketListener","elsedüştü")
                    userMessages[receiverId] = mutableListOf(newArgsModel)
                }

                viewModelScope.launch(Dispatchers.Main) {
                    _isFriendsListRecording.value = userMessages
                }
                viewModelScope.launch(Dispatchers.Main) {
                    _messageMutableLiveEvent.value = argsModel
                }
            }else{
                Log.e("socketOn","else düştü")
            }
        }
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
    fun goToUserProfileFragment(){
        _navigateToDestinationSingleLiveEvent.value = NavigationData(destinationId = R.id.userProfileFragment)
    }

    fun navigateUp() {
        _navigateUpSingleLiveEvent.call()
    }
}