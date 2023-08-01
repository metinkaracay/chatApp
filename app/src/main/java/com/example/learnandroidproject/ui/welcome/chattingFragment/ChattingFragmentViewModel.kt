package com.example.learnandroidproject.ui.welcome.chattingFragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.common.isSuccess
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.SendingMessage
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import com.github.michaelbull.result.get
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class ChattingFragmentViewModel @Inject constructor(private val datingApiRepository: DatingApiRepository): BaseViewModel(){

    private val _chattingPageViewStateLiveData: MutableLiveData<ChattingFragmentPageViewState> = MutableLiveData()
    val chattingPageViewStateLiveData: LiveData<ChattingFragmentPageViewState> = _chattingPageViewStateLiveData

    private val _userLiveData: MutableLiveData<ChattingFragmentPageViewState> = MutableLiveData()
    val userLiveData: LiveData<ChattingFragmentPageViewState> = _userLiveData

    private val _errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val errorMessageLiveData: LiveData<String> = _errorMessageLiveData

    var user: UserInfo = UserInfo(0,"","","null",null,null)

    var messageList: List<MessageItem> = arrayListOf()
    fun getUserInfo(){
        _userLiveData.value = userLiveData.value?.copy(userInfo = user)
    }
    fun fetchMessages(list: List<MessageItem>){
        _chattingPageViewStateLiveData.value = ChattingFragmentPageViewState(user,list)
    }
    init {
        getAllMessages()
    }
    fun getAllMessages(){
        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.getMessages(user.uId.toString()).get()?.let {
                withContext(Dispatchers.Main){
                    messageList = it
                    fetchMessages(it)
                }
            }
        }
    }

    fun getMessages(Socket: SocketHandler, context: Context){
        val sharedPreferences = context.getSharedPreferences("LoggedUserID",Context.MODE_PRIVATE)
        val loggedUserId = sharedPreferences.getString("LoggedUserId","")
        Socket.setSocket(context)
        val mSocket = Socket.getSocket()

        mSocket.connect()

        mSocket.on("newMessage:user:${user.uId}"){ args ->
            if (args[0] != null){
                val message = mutableListOf<MessageItem>()
                Log.e("socketOn","${args[1]}")
                val newMessage = MessageItem(args[0].toString(),args[1].toString(),user.uId.toString())

                viewModelScope.launch(Dispatchers.Main) {
                    messageList = messageList + newMessage

                    fetchMessages(messageList)
                }
            }
        }
        Log.e("userIdTest","${loggedUserId}")
        mSocket.on("newMessage:user:${loggedUserId}"){ args ->
            if (args[0] != null){
                if(args[1].toString() == user.uId.toString()){
                val message = mutableListOf<MessageItem>()
                Log.e("socketOn","${args[1]}")
                val newMessage = MessageItem(args[0].toString(),user.uId.toString(),loggedUserId.toString())

                viewModelScope.launch(Dispatchers.Main) {
                    messageList = messageList + newMessage

                    fetchMessages(messageList)
                }
                }
            }
        }
    }
    fun sendMessage(message: String){

        if (message.isNotEmpty() && message.isNotBlank()){
            viewModelScope.launch(Dispatchers.IO){
                val result = datingApiRepository.sendMessage(user.uId.toString(),SendingMessage(message))

                if (result.isSuccess()){
                    Log.e("postSonuç","mesaj Gönderildi")
                }else{
                    Log.e("postSonuç","Hata")
                }
            }
        }else{
            _errorMessageLiveData.postValue("Lütfen Bir Mesaj Girin")
        }
    }
}