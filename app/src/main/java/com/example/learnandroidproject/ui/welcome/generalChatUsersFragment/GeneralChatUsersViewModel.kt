package com.example.learnandroidproject.ui.welcome.generalChatUsersFragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.data.local.model.dating.db.request.userRequest.User
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import com.example.learnandroidproject.ui.welcome.chattingFragment.SocketHandler
import com.github.michaelbull.result.get
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GeneralChatUsersViewModel @Inject constructor(private val datingApiRepository: DatingApiRepository): BaseViewModel() {

    private val _generalChatUsersPageViewStateLiveData: MutableLiveData<GeneralChatUsersPageViewState> = MutableLiveData()
    val generalChatUsersPageViewStateLiveData: LiveData<GeneralChatUsersPageViewState> = _generalChatUsersPageViewStateLiveData

    var userList: List<UserInfo> = arrayListOf()

    init {
        getAllUsers()
    }

    fun getAllUsers(){

        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.fetchUsersForChatRooms().get()?.let {
                withContext(Dispatchers.Main){
                    _generalChatUsersPageViewStateLiveData.value = GeneralChatUsersPageViewState(it)
                    Log.e("asdf","ilk userlist $it")
                }
            }
        }
    }

    fun getAllUsersLiveData(Socket: SocketHandler, context: Context){
        val sharedPreferences = context.getSharedPreferences("LoggedUserID",Context.MODE_PRIVATE)
        val loggedUserId = sharedPreferences.getString("LoggedUserId","")

        Socket.setSocket(context)
        val mSocket = Socket.getSocket()

        mSocket.connect()

        mSocket.on("newMessage:user:${loggedUserId}"){ args ->

            Log.e("socketOn","${args[2]}")

            // Socketten gelen veriyi JSON formatına dönüştürün
            val jsonString = args[2].toString()

            // Gson ile JSON veriyi kendi UserInfo modelinize göre çözümleyin
            val gson = Gson()
            val userInfoListType = object : TypeToken<List<UserInfo>>() {}.type
            val userList2: List<UserInfo> = gson.fromJson(jsonString, userInfoListType)

            // Elde edilen userList2 verisini kullanabilirsiniz
            // Örneğin:
            for (userInfo in userList2) {
                // userInfo içindeki bilgilere erişebilirsiniz
                val uId = userInfo.uId
                val uName = userInfo.uName
                val uStatu = userInfo.uStatu
                val uPhoto = userInfo.uPhoto
                val lastMessage = userInfo.lastMessage
                val elapsedTime = userInfo.elapsedTime

                val eleman = UserInfo(uId,uName,uStatu,uPhoto,lastMessage,elapsedTime)
                userList = userList + eleman
            }

            Log.e("asdf","$userList")
            Log.e("asdf","$jsonString")
            //_generalChatUsersPageViewStateLiveData.value = GeneralChatUsersPageViewState(userList)
            _generalChatUsersPageViewStateLiveData.postValue(GeneralChatUsersPageViewState(userList))
        }
    }
}