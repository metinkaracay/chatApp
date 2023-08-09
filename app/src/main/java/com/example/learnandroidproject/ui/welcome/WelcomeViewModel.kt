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
import com.google.android.datatransport.cct.internal.LogEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor() : ViewModel() {

    private val _navigateToDestinationSingleLiveEvent: SingleLiveEvent<NavigationData> = SingleLiveEvent()
    private val _navigateUpSingleLiveEvent: SingleLiveEvent<Any?> = SingleLiveEvent()
    private val _closePageSingleLiveEvent: SingleLiveEvent<Any?> = SingleLiveEvent()
    private val _additionalDataSingleLiveEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val _messageSingleLiveEvent: SingleLiveEvent<Args> = SingleLiveEvent()
    private val _isFriendsListRecording: MutableLiveData<MutableMap<String, MutableList<Args>>> = MutableLiveData()
    private val _isMEssageSended: MutableLiveData<Any> = MutableLiveData()

    private val _testSingleLiveEvent: SingleLiveEvent<String> = SingleLiveEvent()

    val closePageSingleLiveEvent: LiveData<Any?> = _closePageSingleLiveEvent
    val navigateToDestinationSingleLiveEvent: LiveData<NavigationData> = _navigateToDestinationSingleLiveEvent
    val navigateUpSingleLiveEvent: LiveData<Any?> = _navigateUpSingleLiveEvent
    val additionalDataSingleLiveEvent: SingleLiveEvent<Boolean> = _additionalDataSingleLiveEvent
    val messageSingleLiveEvent: SingleLiveEvent<Args> = _messageSingleLiveEvent  // TODO isimi düzelt
    val isFriendsListRecording: MutableLiveData<MutableMap<String, MutableList<Args>>> = _isFriendsListRecording
    val isMEssageSended: MutableLiveData<Any> = _isMEssageSended

    val testSingleLiveEvent: LiveData<String> = _testSingleLiveEvent

    private var user: User = User(null, null, null, null, null, null, null,null,null)
    private var userInfo = UserInfo(0,"null","null","null",null,null,false)
    private var clickedUserPhoto: String? = null
    private var isExitChatRoom: Boolean = false
    val userMessages: MutableMap<String, MutableList<Args>> = mutableMapOf()
    var sendedMessages: MutableMap<String, MutableList<Args>> = mutableMapOf()
    private var clickedUsers: MutableList<Int> = arrayListOf()
    private var additionId: String? = null
    private var lastSentMessage: MessageItem? = null

    fun fillLastSentMessage(messageData: MutableMap<String, MutableList<Args>>){
        sendedMessages = messageData
    }

    fun getLastSentMessage(): MutableMap<String, MutableList<Args>>?{
        viewModelScope.launch(Dispatchers.Main) {
            _isMEssageSended.value = ""
        }

        return sendedMessages
    }

    fun fillAdditionalId(id: String){
        Log.e("bildirimdeki","testttt: $id")
        viewModelScope.launch(Dispatchers.IO){
            delay(1000L)
            _additionalDataSingleLiveEvent.postValue(true)
        }
        additionId = id
    }

    fun getAdditionalId(): String?{
        _additionalDataSingleLiveEvent.postValue(false)
        return additionId
    }

    fun getClickedUsersList(): MutableList<Int>{
        return clickedUsers
    }
    fun exitToChatRoomFillData(data: Boolean){
        isExitChatRoom = data
    }
    fun getExitChatRoomData(): Boolean{
        return isExitChatRoom
    }
    fun fillUserData(userName: String, email: String, password: String) {
        user.userName = userName
        user.email = email
        user.password = password
    }
    fun fillUserInfoData(id: Int,name: String, statu: String, photo: String) {
        if (!clickedUsers.contains(id)){
            clickedUsers.add(id)
        }
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
                var argsModel = Args("","","","",false)

                val json = JSONObject(args[0].toString())

                val senderIdFromServer = json.getInt("senderId")
                val message = json.getString("message")
                val date = json.getString("sendTime")
                val receiverIdFromServer = json.getInt("receiverId")
                val isSeen = json.getBoolean("isSeen")

                Log.e("welargs0","${message}")
                //Log.e("welargs","${receiverId2}")
                Log.e("welseen","${date}")
                /*if(args[0].toString() == loggedUserId){
                    argsModel = Args(args[1].toString(),args[0].toString(),args[3].toString(),args[2].toString(),args[4].equals(Boolean))
                    Log.e("weltest","çalıştı")
                }else{
                    Log.e("weltestalıcı","çalıştı")
                    argsModel = Args(args[1].toString(),args[0].toString(),loggedUserId.toString(),args[2].toString(),args[4].equals(Boolean))
                }*/
                if(args[0].toString() == loggedUserId){
                    argsModel = Args(message,senderIdFromServer.toString(),receiverIdFromServer.toString(),date,isSeen.equals(Boolean))
                    Log.e("weltest","çalıştı")
                }else{
                    Log.e("weltestalıcı","çalıştı")
                    argsModel = Args(message,senderIdFromServer.toString(),receiverIdFromServer.toString(),date,isSeen.equals(Boolean))
                }


                val senderId = argsModel.senderId
                val receiverId = argsModel.receiverId
                val messageContent = argsModel.message
                val messageDate = argsModel.messageTime
                val seen = argsModel.seen

                val newArgsModel = Args(messageContent, senderId, receiverId, messageDate,seen)


                GlobalScope.launch {
                    withContext(Dispatchers.Main) {
                        _testSingleLiveEvent.value = messageContent
                    }
                }
                viewModelScope.launch(Dispatchers.Main) {
                }

                /*if (userMessages.containsKey(receiverId)) {
                    Log.e("testREc","$receiverId")
                    userMessages[receiverId]?.add(newArgsModel)
                } else {
                    Log.e("testREc1","$receiverId")
                    userMessages[receiverId] = mutableListOf(newArgsModel)
                }*/

                //userMessages daha önce oluşmamışsa oluştur
                if(userMessages.isEmpty()){
                    Log.e("userMesss","$userMessages")
                    userMessages[receiverId] = mutableListOf(newArgsModel)
                }else{
                    Log.e("userMesss1","$userMessages")
                    userMessages[receiverId]?.add(newArgsModel)
                }

                viewModelScope.launch(Dispatchers.Main) {
                    _isFriendsListRecording.value = userMessages
                }
                viewModelScope.launch(Dispatchers.Main) {
                    Log.e("weltestscope","welcome Çalıştı")
                    _messageSingleLiveEvent.value = argsModel
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
    fun goToFriendsChatUsersFragment(){
        _navigateToDestinationSingleLiveEvent.value = NavigationData(destinationId = R.id.friendsChatUsersFragment)
    }
    fun goToUserProfileFragment(){
        _navigateToDestinationSingleLiveEvent.value = NavigationData(destinationId = R.id.userProfileFragment)
    }

    fun goToSplashActivity(){
        _navigateToDestinationSingleLiveEvent.value = NavigationData(destinationId = R.id.splashActivity)
    }
    fun navigateUp() {
        _navigateUpSingleLiveEvent.call()
    }
}