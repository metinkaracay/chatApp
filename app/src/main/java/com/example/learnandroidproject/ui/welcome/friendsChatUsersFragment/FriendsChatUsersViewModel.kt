package com.example.learnandroidproject.ui.welcome.friendsChatUsersFragment

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.common.SingleLiveEvent
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.Args
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import com.example.learnandroidproject.ui.welcome.generalChatUsersFragment.GeneralChatUsersPageViewState
import com.example.learnandroidproject.ui.welcome.userProfileFragment.UserProfilePageViewState
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
    val clickedUserLastMessage: Boolean? = null
    var clickedUserId = 0
    var clickedUserForCurrentRoom: Int? = null
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

    fun listUpdateForSending(){
        for (i in 0 until friendList.size) {
            val userId = friendList[i].uId.toString()
            var userMessages1 = sendingMessage[userId] // Benim gönderdiğim
            Log.e("sendingMessages","${sendingMessage}")

            var counter = 0

            if (userMessages1 != null) {
                val listSize = userMessages1.size
                Log.e("listSize gönderilen", "$listSize")
                for (message in userMessages1) {
                    if (counter == listSize - 1) {
                        val currentTime = formatMessageTime(message.messageTime)
                        Log.e("Messagefriends", "Sender1: ${message.senderId}, Receiver: ${message.receiverId}, Content: ${message.message}, Date: ${message.messageTime}, Seen: ${message.seen}")

                        if (friendList[i].uId.toString() == message.receiverId) {
                            friendList[i].lastMessage = message.message
                            friendList[i].elapsedTime = currentTime
                            Log.e("gelen seen1", "${message.seen}")
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
                Log.e("yeni fonk","clicked şu : $i")
                Log.e("yeni fonk1","clicked şu : ${clickedUsersList[i]}")
                for (j in 0 until friendList.size) { // TODO duplicate çözülürse sil
                    Log.e("clickedUserListiç","${clickedUsersList[i]}")
                    if (friendList[j].uId == clickedUsersList[i]){
                        Log.e("Eşleşti","bu kişi friendlistte")
                    }else{
                        withoutFriendListUsers(sendingMessage, clickedUsersList[i]) // bu hariç
                    }
                }
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

    fun listUpdate(userMessages: MutableMap<String, MutableList<Args>>, context: Context) {
        val sharedPreferences = context.getSharedPreferences("LoggedUserID", Context.MODE_PRIVATE)
        val loggedUserId = sharedPreferences.getString("LoggedUserId", "")

        var userMessages2 = userMessages[loggedUserId.toString()]
        Log.e("userMessagesçökerten2","$userMessages2") // bana gelen

        for (i in 0 until friendList.size) {
            val userId = friendList[i].uId.toString()
            //var userMessages1 = userMessages[userId] //soketten kendi mesajımı dinlediğimde
            var userMessages1 = sendingMessage[userId] // Benim gönderdiğim
            Log.e("userMessagesçökerten1","$userMessages1")
            /*var userMessages2 = userMessages[loggedUserId.toString()]
            Log.e("usMess2 içi","$userMessages2") // bana gelen*/

            var counter = 0
            val currentMessages: MutableList<Args> = mutableListOf()

            // Karşıdan mesaj geldiğinde çalışır
            if (userMessages2 != null) {
                val listSize = userMessages2.size
                Log.e("listSize gelen", "$listSize")
                for (message in userMessages2) {
                    // Bir kullanıcı birden fazla kez mesaj attığında biriktiriyor. Son elemanı almak için kullanıyoruz burayı
                    if (counter == listSize - 1) {
                        val currentTime = formatMessageTime(message.messageTime)
                        Log.e(
                            "Messagefriends",
                            "Sender: ${message.senderId}, Receiver: ${message.receiverId}, Content: ${message.message}, Date: ${message.messageTime}, Seen: ${message.seen}"
                        )
                        if (friendList[i].uId.toString() == message.senderId) {
                            friendList[i].lastMessage = message.message
                            friendList[i].elapsedTime = currentTime
                            friendList[i].seen = message.seen

                            Log.e("gelen seen", "${message.seen}")
                            //currentMessages.add(message)
                            userMessages2.clear()
                        }
                    }
                    counter++
                }
            }
            // Telefonda oturum açan kullanıcı mesaj attığında çalışır
            /* if (userMessages1 != null) {
                val listSize = userMessages1.size
                Log.e("listSize gönderilen","$listSize")
                for (message in userMessages1) {
                    if (counter == listSize - 1) {
                        val currentTime = formatMessageTime(message.messageTime)
                        Log.e("Messagefriends", "Sender1: ${message.senderId}, Receiver: ${message.receiverId}, Content: ${message.message}, Date: ${message.messageTime}, Seen: ${message.seen}")

                        if (friendList[i].uId.toString() == message.receiverId) {
                            friendList[i].lastMessage = message.message
                            friendList[i].elapsedTime = currentTime
                            Log.e("gelen seen1","${message.seen}")
                            friendList[i].seen = true
                            currentMessages.add(message)
                        }
                    }
                    counter++
                }
            }*/

            /*if (userMessages1 != null) { //TODO son kapatılan 1
                val listSize = userMessages1.size
                Log.e("listSize gönderilen", "$listSize")
                for (message in userMessages1) {
                    if (counter == listSize - 1) {
                        val currentTime = formatMessageTime(message.messageTime)
                        Log.e("Messagefriends", "Sender1: ${message.senderId}, Receiver: ${message.receiverId}, Content: ${message.message}, Date: ${message.messageTime}, Seen: ${message.seen}")

                        if (friendList[i].uId.toString() == message.receiverId) {
                            friendList[i].lastMessage = message.message
                            friendList[i].elapsedTime = currentTime
                            Log.e("gelen seen1", "${message.seen}")
                            friendList[i].seen = true
                            //currentMessages.add(message)
                            userMessages1.clear()

                        }
                    }
                    counter++
                }
            }*/
            /*for (message in currentMessages) {
                userMessages1?.remove(message)
                userMessages2?.remove(message)
            }*/
        }
        // Hata - FriendList'te olmayan birinden mesaj alına yapılan işlemler
        if (userMessages.isNotEmpty()) {
            Log.e("userMessagesçökerten", "$userMessages")// sender
            Log.e("userMessId", "${userMessages[loggedUserId]}")
            Log.e("userMessId", "${userMessages[clickedUserId.toString()]}")
            var senderId: String? = null
            var messageTime: String? = null
            var lastMessage: String? = null

            Log.e("userMesTEst", "çalışmadan önce : ${friendList.size}")
            val userMesReceiver = userMessages[loggedUserId]
            //val userMessageSender = userMessages[clickedUserId.toString()] soketten kendi mesajımı dinlediğimde
            Log.e("userMessages", "$userMesReceiver")
            if (!userMesReceiver.isNullOrEmpty()) {
                Log.e("çökerten açık","çökebiilit")
                for (message in userMesReceiver) {
                    senderId = message.senderId
                    messageTime = formatMessageTime(message.messageTime)
                    lastMessage = message.message
                    userMesReceiver.remove(message)
                }
                Log.e("senderIdKontrol", "$senderId")
                viewModelScope.launch(Dispatchers.IO) {
                    datingApiRepository.getUserProfile(senderId!!).get()?.let {
                        withContext(Dispatchers.Main) {
                            newUserInfo = UserInfo(senderId.toInt(), it.userName!!, it.status!!, it.photo!!, lastMessage, messageTime,false)
                            if (friendList.contains(newUserInfo)) {
                                Log.e("zaten vardı yine geldi1", "$newUserInfo")
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
            }else {
                viewModelScope.launch(Dispatchers.IO) {
                    withContext(Dispatchers.Main) {
                        friendList.sortByDescending { it.elapsedTime }
                        _friendsChatUsersPageViewStateLiveData.postValue(
                            FriendsChatUsersPageViewState(friendList)
                        )
                    }
                }
            }
            friendList.sortByDescending { it.elapsedTime }
            _friendsChatUsersPageViewStateLiveData.postValue(FriendsChatUsersPageViewState(friendList))
            Log.e("testttt", "kimse kalmadı")
            Log.e("userMessag", "şu idli: ${clickedUserId}")
        }
        /*if (!clickedUsersList.isNullOrEmpty()) { // TODO son kapatılan 2
            for (i in 0 until clickedUsersList.size) {
                withoutFriendListUsers(userMessages, clickedUsersList[i])
            }
            Log.e("liste eleman sayısı", "Eleman sayısı : ${clickedUsersList.size}")
            clickedUsersList.clear()
            Log.e("liste eleman sayısı", "Eleman sayısı2p : ${clickedUsersList.size}")
            _listUpdated.postValue(true)
        }*/


        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                friendList.sortByDescending { it.elapsedTime }
                _friendsChatUsersPageViewStateLiveData.postValue(FriendsChatUsersPageViewState(friendList))
            }
        }
    }

    fun withoutFriendListUsers(userMessages: MutableMap<String, MutableList<Args>>,clickedUserId: Int){
        Log.e("fonk çalıştı","çalıştı $clickedUserId")
        //val userMessageSender = userMessages[clickedUserId.toString()] // soketten kendi mesajımı dinlediğimde
        val userMessageSender = sendingMessage[clickedUserId.toString()]
        Log.e("gelenSendingMessageİç","$userMessageSender")

        var messageTime = ""
        var lastMessage = ""

        /*if (!userMessageSender.isNullOrEmpty()){
            var listSize = userMessageSender.size
            var counter = 0
            for (message in userMessageSender){
                if (counter == listSize - 1){
                    messageTime = formatMessageTime(message.messageTime)
                    lastMessage = message.message
                    userMessageSender.remove(message)
                }
                counter++
            }
            viewModelScope.launch(Dispatchers.IO){
                datingApiRepository.getUserProfile(clickedUserId.toString()).get()?.let {
                    withContext(Dispatchers.Main){
                        newUserInfo = UserInfo(clickedUserId,it.userName!!,it.status!!,it.photo!!,lastMessage,messageTime,true)
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
        }*/

        if (!userMessageSender.isNullOrEmpty()){
            var listSize = userMessageSender.size
            var counter = 0
            for (message in userMessageSender){
                if (counter == listSize - 1){
                    messageTime = formatMessageTime(message.messageTime)
                    lastMessage = message.message
                    userMessageSender.remove(message)
                }
                counter++
            }
            viewModelScope.launch(Dispatchers.IO){
                datingApiRepository.getUserProfile(clickedUserId.toString()).get()?.let {
                    withContext(Dispatchers.Main){
                        newUserInfo = UserInfo(clickedUserId,it.userName!!,it.status!!,it.photo!!,lastMessage,messageTime,true)
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

    fun updateSeenStateClickedUser(id: Int?){
        for (i in 0 until friendList.size){
            if (friendList[i].uId == id){
                Log.e("seen düzenlendi id: ","$id")
                friendList[i].seen = true
                clickedUserForCurrentRoom = null
                _listUpdated.postValue(false)
            }
        }
    }

    fun formatMessageTime(messageTime: String): String{
        val dateFormat = SimpleDateFormat("dd/MM/yyyy, HH:mm:ss", Locale.getDefault())

        val messageTime = dateFormat.parse(messageTime)

        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val currentTime = timeFormat.format(messageTime)
        Log.e("friendsFormatTime","$currentTime")
        return currentTime
    }
}