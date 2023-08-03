package com.example.learnandroidproject.ui.welcome.friendsChatUsersFragment

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.Args
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import com.example.learnandroidproject.ui.welcome.generalChatUsersFragment.GeneralChatUsersPageViewState
import com.github.michaelbull.result.get
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FriendsChatUsersViewModel@Inject constructor(private val datingApiRepository: DatingApiRepository): BaseViewModel() {

    private val _friendsChatUsersPageViewStateLiveData: MutableLiveData<FriendsChatUsersPageViewState> = MutableLiveData()
    val friendsChatUsersPageViewStateLiveData: LiveData<FriendsChatUsersPageViewState> = _friendsChatUsersPageViewStateLiveData

    var friendList: MutableList<UserInfo> = mutableListOf<UserInfo>()
    init {
        getAllUsers()
    }

    fun getAllUsers(){

        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.fetchFriendsUsers().get()?.let {
                withContext(Dispatchers.Main){
                    friendList.addAll(it)
                    _friendsChatUsersPageViewStateLiveData.value = FriendsChatUsersPageViewState(it)
                }
            }
        }
    }
    fun sortFriendList(args: Args, context: Context){
        val dateFormat = SimpleDateFormat("dd/MM/yyyy, HH:mm:ss", Locale.getDefault())
        Log.e("gelenmeszaman","${args.messageTime}")
        try {
            if (args.messageTime.matches(Regex("\\d{2}:\\d{2}"))){
                Log.e("sortFriendList","ife girdi")
                sortingList(args,args.messageTime,context)
            }else{
                Log.e("sortfriendlist","else girdi")
                val messageTime = dateFormat.parse(args.messageTime)

                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val currentTime = timeFormat.format(messageTime)
                Log.e("sıralanmışlisteöncesi","${args.message}")
                sortingList(args,currentTime,context)
            }
        } catch (e: Exception) {
            Toast.makeText(context,"Geçersiz tarih formatı veya hata",Toast.LENGTH_SHORT).show()
        }
    }
    fun sortingList(args: Args,currentTime:String, context: Context){
        val sharedPreferences = context.getSharedPreferences("LoggedUserID",Context.MODE_PRIVATE)
        val loggedUserId = sharedPreferences.getString("LoggedUserId","")
        Log.e("iföncesi","${args.senderId}")
        Log.e("iföncesi2","${args.receiverId}")
        Log.e("iföncesi3","${loggedUserId}")

        for (i in 0 until friendList.size){
            if (args.senderId.toInt() == friendList.get(i).uId){
                Log.e("forrrrr","çalıştı")
                val newCurrent = formatMessageTime(args.messageTime)
                friendList.get(i).lastMessage = args.message
                //friendList.get(i).elapsedTime = currentTime
                friendList.get(i).elapsedTime = newCurrent
            }else if(args.senderId == loggedUserId) {
                if (args.receiverId.toInt() == friendList.get(i).uId) {
                    val newCurrent = formatMessageTime(args.messageTime)
                    friendList.get(i).lastMessage = args.message
                    //friendList.get(i).elapsedTime = currentTime
                    friendList.get(i).elapsedTime = newCurrent
                }
            }
            Log.e("Foritem","${friendList.get(i).elapsedTime}")
        }
        friendList.sortByDescending { it.elapsedTime }
        Log.e("sıralanmışlisteyanı","${args.message}")
        Log.e("sıralanmış liste","$friendList")
        _friendsChatUsersPageViewStateLiveData.value = FriendsChatUsersPageViewState(friendList)
    }

    /*fun listUpdate(userMessages: MutableMap<String, MutableList<Args>>, context: Context){
        //GEREK KALMADI
        val sharedPreferences = context.getSharedPreferences("LoggedUserID",Context.MODE_PRIVATE)
        val loggedUserId = sharedPreferences.getString("LoggedUserId","")

        for (i in 0 until friendList.size) {
            val userId = friendList.get(i).uId.toString()
            Log.e("useridd", "$userId")
            var userMessages1 = userMessages[userId]
            var counter = 0

            if (userMessages1 != null) {
                val listSize = userMessages1.size
                for (message in userMessages1) {
                    if (counter == listSize - 1) {
                        val currentTime = formatMessageTime(message.messageTime)
                        // Mesajları işleme koyma işlemleri burada yapılabilir.
                        Log.e("Messagefriends", "Sender: ${message.senderId}, Receiver: ${message.receiverId}, Content: ${message.message}, Date: ${message.messageTime}")
                        if (friendList.get(i).uId.toString() == message.receiverId) {
                            friendList.get(i).lastMessage = message.message
                            friendList.get(i).elapsedTime = currentTime
                        }
                    }
                    counter++
                }
            }else{
                userMessages1 = userMessages[loggedUserId.toString()]

                if (userMessages1 != null){
                    val listSize = userMessages1.size
                    for (message in userMessages1){
                        if (counter == listSize-1){
                            val currentTime = formatMessageTime(message.messageTime)
                            Log.e("Messagefriends", "Sender: ${message.senderId}, Receiver: ${message.receiverId}, Content: ${message.message}, Date: ${message.messageTime}")
                            if (friendList.get(i).uId.toString() == message.senderId) {
                                friendList.get(i).lastMessage = message.message
                                friendList.get(i).elapsedTime = currentTime
                            }
                        }
                        counter++
                    }
                }
            }
        }
        friendList.sortByDescending { it.elapsedTime }
        _friendsChatUsersPageViewStateLiveData.value = FriendsChatUsersPageViewState(friendList)
    }*/

    /*fun listUpdate(userMessages: MutableMap<String, MutableList<Args>>, context: Context){
        //GEREK KALMADI
        val sharedPreferences = context.getSharedPreferences("LoggedUserID",Context.MODE_PRIVATE)
        val loggedUserId = sharedPreferences.getString("LoggedUserId","")

        for (i in 0 until friendList.size) {
            val userId = friendList.get(i).uId.toString()
            var userMessages1 = userMessages[userId]
            Log.e("useridd", "$userId")
            var userMessages2 = userMessages[loggedUserId.toString()]

            var counter = 0

            if (userMessages2 != null) {
                 val listSize = userMessages2.size
                    for (message in userMessages2){
                        if (counter == listSize-1){
                            val currentTime = formatMessageTime(message.messageTime)
                            Log.e("Messagefriends", "Sender: ${message.senderId}, Receiver: ${message.receiverId}, Content: ${message.message}, Date: ${message.messageTime}")
                            if (friendList.get(i).uId.toString() == message.senderId) {
                                friendList.get(i).lastMessage = message.message
                                friendList.get(i).elapsedTime = currentTime
                                //userMessages2.remove(message)
                            }
                        }
                        counter++
                    }
            }
            if (userMessages1 != null) {
                    val listSize = userMessages1.size
                    for (message in userMessages1) {
                        if (counter == listSize - 1) {
                            val currentTime = formatMessageTime(message.messageTime)
                            // Mesajları işleme koyma işlemleri burada yapılabilir.
                            Log.e("Messagefriends", "Sender1: ${message.senderId}, Receiver: ${message.receiverId}, Content: ${message.message}, Date: ${message.messageTime}")
                            if (friendList.get(i).uId.toString() == message.receiverId) {
                                friendList.get(i).lastMessage = message.message
                                friendList.get(i).elapsedTime = currentTime
                                userMessages1.remove(message)
                            }
                        }
                        counter++
                    }
                }
            }
        friendList.sortByDescending { it.elapsedTime }
        _friendsChatUsersPageViewStateLiveData.value = FriendsChatUsersPageViewState(friendList)
    }*/

    fun listUpdate(userMessages: MutableMap<String, MutableList<Args>>, context: Context) {
        val sharedPreferences = context.getSharedPreferences("LoggedUserID", Context.MODE_PRIVATE)
        val loggedUserId = sharedPreferences.getString("LoggedUserId", "")

        for (i in 0 until friendList.size) {
            val userId = friendList[i].uId.toString()
            var userMessages1 = userMessages[userId]
            var userMessages2 = userMessages[loggedUserId.toString()]

            var counter = 0
            val currentMessages: MutableList<Args> = mutableListOf()

            if (userMessages2 != null) {
                val listSize = userMessages2.size
                for (message in userMessages2) {
                    // Bir kullanıcı birden fazla kez mesaj attığında biriktiriyor. Son elemanı almak için kullanıyoruz burayı
                    if (counter == listSize - 1) {
                        val currentTime = formatMessageTime(message.messageTime)
                        if (friendList[i].uId.toString() == message.senderId) {
                            friendList[i].lastMessage = message.message
                            friendList[i].elapsedTime = currentTime
                            currentMessages.add(message)
                        }
                    }
                    counter++
                }
            }
            if (userMessages1 != null) {
                val listSize = userMessages1.size
                for (message in userMessages1) {
                    if (counter == listSize - 1) {
                        val currentTime = formatMessageTime(message.messageTime)
                        if (friendList[i].uId.toString() == message.receiverId) {
                            friendList[i].lastMessage = message.message
                            friendList[i].elapsedTime = currentTime
                            currentMessages.add(message)
                        }
                    }
                    counter++
                }
            }
            for (message in currentMessages) {
                userMessages1?.remove(message)
                userMessages2?.remove(message)
            }
        }
        friendList.sortByDescending { it.elapsedTime }
        _friendsChatUsersPageViewStateLiveData.value = FriendsChatUsersPageViewState(friendList)
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