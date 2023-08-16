package com.example.learnandroidproject.ui.welcome.groupChatsFragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.Args
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupInfo
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import com.example.learnandroidproject.ui.welcome.friendsChatUsersFragment.FriendsChatUsersPageViewState
import com.github.michaelbull.result.get
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class GroupChatsViewModel@Inject constructor(private val datingApiRepository: DatingApiRepository) : BaseViewModel() {

    private val _groupChatsPageViewStateLiveData: MutableLiveData<GroupChatsPageViewState> = MutableLiveData()
    val groupChatsPageViewStateLiveData: LiveData<GroupChatsPageViewState> = _groupChatsPageViewStateLiveData

    private val _listUpdated: MutableLiveData<Boolean> = MutableLiveData()
    val listUpdated: LiveData<Boolean> = _listUpdated

    var groupList: MutableList<GroupInfo> = mutableListOf<GroupInfo>()

    init {
        _listUpdated.postValue(true)
        getAllGroups()
    }

    fun getAllGroups(){
        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.fetchGroups().get()?.let {
                viewModelScope.launch(Dispatchers.Main){
                    _groupChatsPageViewStateLiveData.value = GroupChatsPageViewState(it)
                    groupList.clear()
                    groupList.addAll(it)
                }
            }
        }
    }

    fun fetchGroupListWithNewGroups(list: List<GroupInfo>){
        groupList.clear()
        groupList.addAll(list)
        _groupChatsPageViewStateLiveData.value = GroupChatsPageViewState(groupList)
    }

    fun updateSeenInfo(id: Int) {
        if (id != 0) {
            viewModelScope.launch(Dispatchers.IO) {
                datingApiRepository.updateSeenInfoForGroup(id.toString())
            }
        }
    }
    fun listUpdate(userMessages: MutableMap<String, MutableList<Args>>, context: Context) {
        val sharedPreferences = context.getSharedPreferences("LoggedUserID", Context.MODE_PRIVATE)
        val loggedUserId = sharedPreferences.getString("LoggedUserId","")

        val processedGroupIds = mutableListOf<String>()

        for (i in 0 until groupList.size) {
            val groupId = groupList[i].groupId
            var userMessages1 = userMessages[groupId.toString()] //soketten kendi mesajımı dinlediğimde
            var counter = 0

            // Karşıdan mesaj geldiğinde çalışır
            if (userMessages1 != null) {
                val listSize = userMessages1.size
                for (message in userMessages1) {
                    // Bir kullanıcı birden fazla kez mesaj attığında biriktiriyor. Mapin son elemanını almak için kullanıyoruz burayı
                    if (counter == listSize - 1) {
                        val currentTime = formatMessageTime(message.messageTime)
                        Log.e("Messagefriends", "Sender: ${message.senderId}, Receiver: ${message.receiverId}, Content: ${message.message}, Date: ${message.messageTime}, Seen: ${message.seen}")
                        if (groupList[i].groupId.toString() == message.receiverId) {
                            groupList[i].lastMessage = message.message
                            groupList[i].messageTime = currentTime
                            if (loggedUserId == message.senderId){
                                groupList[i].isSeen == true
                            }else{
                                groupList[i].isSeen = message.seen
                            }
                            //currentMessages.add(message)
                            processedGroupIds.add(groupId.toString())
                            userMessages1.clear()
                        }
                    }
                    counter++
                }
            }
        }
        processedGroupIds.forEach {
            userMessages.remove(it)
        }
        // FriendList'te olmayan birinden mesaj alına yapılan işlemler
        if (userMessages.isNotEmpty()) {
            Log.e("userMessId", "${userMessages}")

            viewModelScope.launch(Dispatchers.IO) {
                datingApiRepository.fetchGroups().get()?.let {
                    withContext(Dispatchers.Main) {
                        _groupChatsPageViewStateLiveData.value = GroupChatsPageViewState(it)
                        groupList.clear()
                        groupList.addAll(it)
                    }
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                groupList.sortByDescending { it.messageTime }
                _groupChatsPageViewStateLiveData.postValue(GroupChatsPageViewState(groupList))
            }
        }
    }

    // Bir chatten çıkıldığında o chati görüldü yapar
    fun updateSeenStateClickedUser(id: Int?){
        for (i in 0 until groupList.size){
            if (groupList[i].groupId == id){
                Log.e("seen düzenlendi id: ","$id")
                groupList[i].isSeen = true
                _listUpdated.postValue(false)
            }
        }
    }
    // Sunucudan gelen mesaj tarihlerini client için uygun formata çevirir
    fun formatMessageTime(messageTime: String): String{
        val dateFormat = SimpleDateFormat("dd/MM/yyyy, HH:mm:ss", Locale.getDefault())

        val messageTime = dateFormat.parse(messageTime)

        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val currentTime = timeFormat.format(messageTime)
        Log.e("friendsFormatTime","$currentTime")
        return currentTime
    }
}