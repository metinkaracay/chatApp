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
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupMember
import com.example.learnandroidproject.ui.common.navigation.NavigationData
import com.example.learnandroidproject.ui.welcome.chattingFragment.SocketHandler
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
    private val _groupMessageSingleLiveEvent: SingleLiveEvent<Args> = SingleLiveEvent()
    private val _isFriendsListRecording: MutableLiveData<MutableMap<String, MutableList<Args>>> = MutableLiveData()
    private val _isGroupListRecording: MutableLiveData<MutableMap<String, MutableList<Args>>> = MutableLiveData()
    private val _isMessageSended: MutableLiveData<Any> = MutableLiveData()
    private val _isNewGroupCreated: SingleLiveEvent<Boolean> = SingleLiveEvent()

    private val _testSingleLiveEvent: SingleLiveEvent<String> = SingleLiveEvent()

    val closePageSingleLiveEvent: LiveData<Any?> = _closePageSingleLiveEvent
    val navigateToDestinationSingleLiveEvent: LiveData<NavigationData> = _navigateToDestinationSingleLiveEvent
    val navigateUpSingleLiveEvent: LiveData<Any?> = _navigateUpSingleLiveEvent
    val additionalDataSingleLiveEvent: SingleLiveEvent<Boolean> = _additionalDataSingleLiveEvent
    val messageSingleLiveEvent: SingleLiveEvent<Args> = _messageSingleLiveEvent
    val groupMessageSingleLiveEvent: SingleLiveEvent<Args> = _groupMessageSingleLiveEvent
    val isFriendsListRecording: MutableLiveData<MutableMap<String, MutableList<Args>>> = _isFriendsListRecording
    val isGroupListRecording: MutableLiveData<MutableMap<String, MutableList<Args>>> = _isGroupListRecording
    val isMessageSended: MutableLiveData<Any> = _isMessageSended
    val isNewGroupCreated: SingleLiveEvent<Boolean> = _isNewGroupCreated

    val testSingleLiveEvent: LiveData<String> = _testSingleLiveEvent


    private var user: User = User(null, null, null, null, null, null, null,null,null)
    private var userInfo = UserInfo(0,"null","null","null",null,null,false)
    private var groupInfo = GroupInfo(0,"null","null","null","null",null)
    private var clickedUserPhoto: String? = null
    private var isExitChatRoom: Boolean = false
    val groupMessages: MutableMap<String, MutableList<Args>> = mutableMapOf()
    val userMessages: MutableMap<String, MutableList<Args>> = mutableMapOf()
    var sendedMessages: MutableMap<String, MutableList<Args>> = mutableMapOf()
    private var clickedUsers: MutableList<Int> = arrayListOf()
    private var additionId: String? = null
    private var currentFragment: Int? = null
    private var selectedUsersForGroupChat: List<UserInfo> = arrayListOf()
    private var newGroupListResponse: List<GroupInfo> = arrayListOf()// Yeni grup oluşturduğumuz
    var membersList: List<GroupMember> = arrayListOf()

    fun fillNewGroupListResponse(list: List<GroupInfo>){
        _isNewGroupCreated.value = true
        newGroupListResponse = list
    }

    fun getNewGroupListResponse(): List<GroupInfo>{
        return newGroupListResponse
    }

    fun fillSelectedUsersForGroupChat(userList: List<UserInfo>){
        selectedUsersForGroupChat = userList
    }

    fun getSelectedUsersForGroupChat(): List<UserInfo>{
        return selectedUsersForGroupChat
    }

    fun onDestinationChanged(destinationId: Int) {
        currentFragment = destinationId
    }

    fun fillTestSingleEvent(text: String){
        Log.e("test_single_Live","gelen text : ${text}")
        _testSingleLiveEvent.value = text
    }

    fun fillLastSentMessage(messageData: MutableMap<String, MutableList<Args>>){
        sendedMessages = messageData
    }

    fun getLastSentMessage(): MutableMap<String, MutableList<Args>>?{
        viewModelScope.launch(Dispatchers.Main) {
            _isMessageSended.value = ""
        }
        return sendedMessages
    }

    fun fillAdditionalId(id: String){
        Log.e("şu anki fragment","$currentFragment")
        if(currentFragment == R.id.chattingFragment){
            navigateUp()
        }

        viewModelScope.launch(Dispatchers.IO){
            delay(1000L) // Bildirime tıklandığında chatin açılması için friendList'in yüklenmesini bekliyoruz
            withContext(Dispatchers.Main) {
                _additionalDataSingleLiveEvent.postValue(true)
            }
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

    fun fillGroupInfoData(id: Int,gName: String, photo: String) {
        if (!clickedUsers.contains(id)){
            clickedUsers.add(id)
        }
        groupInfo.groupId = id
        groupInfo.groupName = gName
        groupInfo.groupPhoto = photo
    }
    fun getGroupInfo(): GroupInfo {
        return groupInfo
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

        Socket.setSocket(context)
        Socket.establishConnection()
        val mSocket = Socket.getSocket()

        mSocket.on("message"){ args ->
            if (args[0] != null){
                val json = JSONObject(args[0].toString())

                val senderIdFromServer = json.getInt("senderId")
                val message = json.getString("message")
                val messageDate = json.getString("sendTime")
                val receiverIdFromServer = json.getInt("receiverId")
                val isSeen = json.getBoolean("isSeen")


                val argsModel = Args(message, senderIdFromServer.toString(), receiverIdFromServer.toString(), messageDate ,isSeen.equals(Boolean))

                //userMessages daha önce oluşmamışsa oluştur
                if(userMessages.isEmpty()){
                    Log.e("userMesss","$userMessages")
                    userMessages[receiverIdFromServer.toString()] = mutableListOf(argsModel)
                }else{
                    userMessages[receiverIdFromServer.toString()]?.add(argsModel)
                    Log.e("userMesss1","$userMessages")
                }

                viewModelScope.launch(Dispatchers.Main) {
                    _isFriendsListRecording.value = userMessages
                }
                viewModelScope.launch(Dispatchers.Main) {
                    _messageSingleLiveEvent.value = argsModel
                }
            }else{
                Log.e("socketOn","else düştü")
            }
        }

        mSocket.on("message:group"){ args ->
            if (args[0] != null){
                val json = JSONObject(args[0].toString())

                val senderIdFromServer = json.getInt("senderId")
                val message = json.getString("message")
                val messageDate = json.getString("sendTime")
                val receiverIdFromServer = json.getInt("receiverId")
                val isSeen = json.getBoolean("isSeen")

                val argsModel = Args(message, senderIdFromServer.toString(), receiverIdFromServer.toString(), messageDate ,isSeen.equals(Boolean))

                if (groupMessages.containsKey(receiverIdFromServer.toString())) {
                    Log.e("socketListener","ifedüştü")
                    groupMessages[receiverIdFromServer.toString()]?.add(argsModel)
                } else {
                    Log.e("socketListener","elsedüştü")
                    groupMessages[receiverIdFromServer.toString()] = mutableListOf(argsModel)
                }
                viewModelScope.launch(Dispatchers.Main) {
                    _isGroupListRecording.value = groupMessages
                }
                viewModelScope.launch(Dispatchers.Main) {
                    _groupMessageSingleLiveEvent.value = argsModel
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
    fun goToChattingPage() {
        _navigateToDestinationSingleLiveEvent.value = NavigationData(destinationId = R.id.chattingFragment)
    }
    fun goToGroupChattingPage() {
        _navigateToDestinationSingleLiveEvent.value = NavigationData(destinationId = R.id.groupChattingFragment)
    }
    fun goToGenerelChatUsersFragment(){
        _navigateToDestinationSingleLiveEvent.value = NavigationData(destinationId = R.id.generalChatUsersFragment)
    }
    fun goToCreateGroupFragment(){
        _navigateToDestinationSingleLiveEvent.value = NavigationData(destinationId = R.id.createGroupFragment)
    }
    fun goToCompleteGroupFragment(){
        _navigateToDestinationSingleLiveEvent.value = NavigationData(destinationId = R.id.completeGroupCreateFragment)
    }
    fun goToUserProfileFragment(){
        _navigateToDestinationSingleLiveEvent.value = NavigationData(destinationId = R.id.userProfileFragment)
    }
    fun goToChatInfoFragment(){
        _navigateToDestinationSingleLiveEvent.value = NavigationData(destinationId = R.id.chatInfoFragment)
    }

    fun goToSplashActivity(){
        _navigateToDestinationSingleLiveEvent.value = NavigationData(destinationId = R.id.splashActivity)
    }
    fun goToRaceFragment(){
        _navigateToDestinationSingleLiveEvent.value = NavigationData(destinationId = R.id.raceFragment)
    }
    fun navigateUp() {
        _navigateUpSingleLiveEvent.call()
    }
}