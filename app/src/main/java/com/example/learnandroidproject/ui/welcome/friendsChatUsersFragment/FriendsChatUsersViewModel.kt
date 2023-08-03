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
                    //friendList.sortByDescending { it.elapsedTime }
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
                //sortingList(args.receiverId,args.message, args.messageTime)
                sortingList(args,args.messageTime,context)
            }else{
                Log.e("sortfriendlist","else girdi")
                val messageTime = dateFormat.parse(args.messageTime)

                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val currentTime = timeFormat.format(messageTime)
                Log.e("sıralanmışlisteöncesi","${args.message}")
                //sortingList(args.senderId,args.message,currentTime)
                sortingList(args,currentTime,context)
            }
        } catch (e: Exception) {
            Toast.makeText(context,"Geçersiz tarih formatı veya hata",Toast.LENGTH_SHORT).show()
        }
    }
    /*fun sortingList(uId:String,lastMessage: String,currentTime:String){

        Log.e("iföncesi","$uId")
        for (i in 0 until friendList.size){
            if (uId.toInt() == friendList.get(i).uId){
                Log.e("forrrrr","çalıştı")
                friendList.get(i).lastMessage = lastMessage
                friendList.get(i).elapsedTime = currentTime
            }
            Log.e("Foritem","${friendList.get(i).elapsedTime}")
        }
        friendList.sortByDescending { it.elapsedTime }
        Log.e("sıralanmışlisteyanı","$lastMessage")
        Log.e("sıralanmış liste","$friendList")
        _friendsChatUsersPageViewStateLiveData.value = FriendsChatUsersPageViewState(friendList)
    }*/
    fun sortingList(args: Args,currentTime:String, context: Context){
        val sharedPreferences = context.getSharedPreferences("LoggedUserID",Context.MODE_PRIVATE)
        val loggedUserId = sharedPreferences.getString("LoggedUserId","")
        Log.e("iföncesi","${args.senderId}")
        Log.e("iföncesi2","${args.receiverId}")
        Log.e("iföncesi3","${loggedUserId}")

        for (i in 0 until friendList.size){
            if (args.senderId.toInt() == friendList.get(i).uId){
                Log.e("forrrrr","çalıştı")
                friendList.get(i).lastMessage = args.message
                friendList.get(i).elapsedTime = currentTime
            }else if(args.senderId == loggedUserId) {
                if (args.receiverId.toInt() == friendList.get(i).uId) {
                    friendList.get(i).lastMessage = args.message
                    friendList.get(i).elapsedTime = currentTime
                }
            }
            Log.e("Foritem","${friendList.get(i).elapsedTime}")
        }
        friendList.sortByDescending { it.elapsedTime }
        Log.e("sıralanmışlisteyanı","${args.message}")
        Log.e("sıralanmış liste","$friendList")
        _friendsChatUsersPageViewStateLiveData.value = FriendsChatUsersPageViewState(friendList)
    }
}