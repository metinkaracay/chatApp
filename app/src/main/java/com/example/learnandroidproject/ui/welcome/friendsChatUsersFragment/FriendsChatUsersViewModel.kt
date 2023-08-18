package com.example.learnandroidproject.ui.welcome.friendsChatUsersFragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.Args
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import com.github.michaelbull.result.get
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FriendsChatUsersViewModel@Inject constructor(private val datingApiRepository: DatingApiRepository): BaseViewModel() {

    private val _friendsChatUsersPageViewStateLiveData: MutableLiveData<FriendsChatUsersPageViewState> = MutableLiveData()
    val friendsChatUsersPageViewStateLiveData: LiveData<FriendsChatUsersPageViewState> = _friendsChatUsersPageViewStateLiveData

    private val _listUpdated: MutableLiveData<Boolean> = MutableLiveData()
    val listUpdated: LiveData<Boolean> = _listUpdated

    private val _notificationUserFilled: MutableLiveData<Boolean> = MutableLiveData()
    val notificationUserFilled: LiveData<Boolean> = _notificationUserFilled


    var friendList: MutableList<UserInfo> = mutableListOf<UserInfo>()
    var notificationUser: UserInfo? = null
    var clickedUsersList: MutableList<Int> = mutableListOf()
    var newUserInfo = UserInfo(0,"","","","","",true)
    var sendingMessage: MutableMap<String, MutableList<Args>> = mutableMapOf()

    init {
        getAllUsers()
    }

    fun getAllUsers(){

        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.fetchFriendsUsers().get()?.let {
                withContext(Dispatchers.Main){
                    Log.e("fetchGelen","${it}")
                    friendList.clear()
                    friendList.addAll(it)
                    _friendsChatUsersPageViewStateLiveData.value = FriendsChatUsersPageViewState(it)
                }
            }
        }
    }

    fun findUserForNotification(id: String){
        viewModelScope.launch(Dispatchers.IO){
            delay(300L)

            Log.e("bildirimdeki liste","${friendList}")
            if (!friendList.isNullOrEmpty()){
                for (i in 0 until friendList.size){
                    if (friendList[i].uId == id.toInt()){
                        notificationUser = UserInfo(id.toInt(),friendList[i].uName,friendList[i].uStatu,friendList[i].uPhoto,friendList[i].lastMessage,friendList[i].elapsedTime,friendList[i].seen)
                        _notificationUserFilled.postValue(true)
                    }
                }
            }
        }
    }

    fun fixObserver(){
        // Looptan kurtarmak için
        _notificationUserFilled.postValue(false)
    }
    fun updateSeenInfo(id: Int){
        if (id != 0){
            viewModelScope.launch(Dispatchers.IO){
                datingApiRepository.updateSeenInfoForUser(id.toString())
            }
        }
    }
    // Benim gönderdiğim mesajlarda çalışır
    fun listUpdateForSending(){
        for (i in 0 until friendList.size) {
            val userId = friendList[i].uId.toString()
            var userMessages1 = sendingMessage[userId] // Benim gönderdiğim
            Log.e("sendingMessages","${userMessages1}")

            var counter = 0

            if (userMessages1 != null) {
                val listSize = userMessages1.size
                for (message in userMessages1) {
                    if (counter == listSize - 1) {
                        Log.e("Messagefriends", "Sender1: ${message.senderId}, Receiver: ${message.receiverId}, Content: ${message.message}, Date: ${message.messageTime}, Seen: ${message.seen}")

                        if (friendList[i].uId.toString() == message.receiverId) {
                            friendList[i].lastMessage = message.message
                            friendList[i].elapsedTime = message.messageTime
                            friendList[i].seen = true
                            userMessages1.clear()
                        }
                    }
                    counter++
                }
            }
        }
        if (!clickedUsersList.isNullOrEmpty()) {
            for (i in 0 until clickedUsersList.size) {
                withoutFriendListUsers(sendingMessage, clickedUsersList[i])
            }
            clickedUsersList.clear()
            _listUpdated.postValue(true)
        }

        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                friendList.sortByDescending { it.elapsedTime }
                _friendsChatUsersPageViewStateLiveData.postValue(FriendsChatUsersPageViewState(friendList))
            }
        }
    }
    // Bana gelen mesajlarda çalışır
    fun listUpdate(userMessages: MutableMap<String, MutableList<Args>>, context: Context) {
        val sharedPreferences = context.getSharedPreferences("LoggedUserID", Context.MODE_PRIVATE)
        val loggedUserId = sharedPreferences.getString("LoggedUserId", "")

        var userMessages2 = userMessages[loggedUserId.toString()]

        for (i in 0 until friendList.size) {
            //var userMessages1 = userMessages[userId] //soketten kendi mesajımı dinlediğimde
            var counter = 0

            // Karşıdan mesaj geldiğinde çalışır
            if (userMessages2 != null) {
                val listSize = userMessages2.size
                for (message in userMessages2) {
                    // Bir kullanıcı birden fazla kez mesaj attığında biriktiriyor. Mapin son elemanını almak için kullanıyoruz burayı
                    if (counter == listSize - 1) {
                        Log.e("Messagefriends", "Sender: ${message.senderId}, Receiver: ${message.receiverId}, Content: ${message.message}, Date: ${message.messageTime}, Seen: ${message.seen}")
                        if (friendList[i].uId.toString() == message.senderId) {
                            friendList[i].lastMessage = message.message
                            friendList[i].elapsedTime = message.messageTime//currentTime
                            friendList[i].seen = message.seen
                            userMessages2.clear()
                        }
                    }
                    counter++
                }
            }
        }
        // FriendList'te olmayan birinden mesaj alına yapılan işlemler
        if (userMessages.isNotEmpty()) {
            Log.e("userMessagesçökerten", "$userMessages")// sender
            Log.e("userMessId", "${userMessages[loggedUserId]}")
            var senderId: String? = null
            var messageTime: String? = null
            var lastMessage: String? = null

            val userMesReceiver = userMessages[loggedUserId]
            //val userMessageSender = userMessages[clickedUserId.toString()] soketten kendi mesajımı dinlediğimde
            Log.e("userMessages", "$userMesReceiver")
            if (!userMesReceiver.isNullOrEmpty()) {
                for (message in userMesReceiver) {
                    senderId = message.senderId
                    messageTime = message.messageTime
                    lastMessage = message.message
                    userMesReceiver.clear()

                }
                viewModelScope.launch(Dispatchers.IO) {
                    datingApiRepository.getUserProfile(senderId!!).get()?.let {
                        withContext(Dispatchers.Main) {
                            newUserInfo = UserInfo(senderId.toInt(), it.userName!!, it.status!!, it.photo!!, lastMessage, messageTime,false)
                            if (friendList.contains(newUserInfo)) {
                                Log.e("zaten vardı yine geldi", "$newUserInfo")
                            } else {
                                friendList.add(newUserInfo)
                            }
                            Log.e("userMesTEst", "çalışmış olmalı : ${friendList.size}")
                            withContext(Dispatchers.Main) {
                                friendList.sortByDescending { it.elapsedTime }
                                _friendsChatUsersPageViewStateLiveData.postValue(
                                    FriendsChatUsersPageViewState(friendList)
                                )
                            }
                        }
                    }
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                Log.e("friendlissst","$friendList")

                friendList.sortByDescending { it.elapsedTime }
                _friendsChatUsersPageViewStateLiveData.postValue(FriendsChatUsersPageViewState(friendList))
            }
        }
    }
    // FriendListte olmayan birine ben mesaj attığımda çalışır
    fun withoutFriendListUsers(userMessages: MutableMap<String, MutableList<Args>>,clickedUser: Int){
        Log.e("fonk çalıştı","çalıştı $clickedUser")
        //val userMessageSender = userMessages[clickedUserId.toString()] // soketten kendi mesajımı dinlediğimde
        val userMessageSender = sendingMessage[clickedUser.toString()]

        var messageTime = ""
        var lastMessage = ""

        if (!userMessageSender.isNullOrEmpty()){
            var listSize = userMessageSender.size
            var counter = 0
            for (message in userMessageSender){
                if (counter == listSize - 1){
                    messageTime =  message.messageTime
                    lastMessage = message.message
                    userMessageSender.remove(message)
                }
                counter++
            }
            viewModelScope.launch(Dispatchers.IO){
                datingApiRepository.getUserProfile(clickedUser.toString()).get()?.let {
                    withContext(Dispatchers.Main){
                        newUserInfo = UserInfo(clickedUser,it.userName!!,it.status!!,it.photo!!,lastMessage,messageTime,true)
                        if (friendList.contains(newUserInfo)){
                            Log.e("zaten vardı yine geldi2","$newUserInfo")
                        }else{
                            friendList.add(newUserInfo)
                        }
                        Log.e("userMesTEstSen","çalışmış olmalı sayı : ${friendList.size}")
                        withContext(Dispatchers.Main){
                            friendList.sortByDescending { it.elapsedTime }
                            _friendsChatUsersPageViewStateLiveData.postValue(FriendsChatUsersPageViewState(friendList))
                        }
                    }
                }
            }
        }
    }
    // Bir chatten çıkıldığında o chati görüldü yapar
    fun updateSeenStateClickedUser(id: Int?){
        for (i in 0 until friendList.size){
            if (friendList[i].uId == id){
                Log.e("seen düzenlendi id: ","$id")
                friendList[i].seen = true
                _listUpdated.postValue(false)
            }
        }
    }
}