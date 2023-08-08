package com.example.learnandroidproject.ui.welcome.chattingFragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.common.SingleLiveEvent
import com.example.learnandroidproject.common.isSuccess
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.Args
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.SendingMessage
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import com.example.learnandroidproject.ui.welcome.chattingFragment.adapter.MessageItemPageViewState
import com.example.learnandroidproject.ui.welcome.friendsChatUsersFragment.FriendsChatUsersPageViewState
import com.github.michaelbull.result.get
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
@HiltViewModel
class ChattingFragmentViewModel @Inject constructor(private val datingApiRepository: DatingApiRepository): BaseViewModel(){

    private val _chattingPageViewStateLiveData: MutableLiveData<ChattingFragmentPageViewState> = MutableLiveData()
    val chattingPageViewStateLiveData: LiveData<ChattingFragmentPageViewState> = _chattingPageViewStateLiveData

    private val _userLiveData: MutableLiveData<ChattingFragmentPageViewState> = MutableLiveData()
    val userLiveData: LiveData<ChattingFragmentPageViewState> = _userLiveData

    private val _errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val errorMessageLiveData: LiveData<String> = _errorMessageLiveData

    private val _sendingMessageArgsLiveData: MutableLiveData<Args> = MutableLiveData()
    val sendingMessageArgsLiveData: LiveData<Args> = _sendingMessageArgsLiveData

    private val _messageFetchRequestLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val messageFetchRequestLiveData: LiveData<Boolean> = _messageFetchRequestLiveData

    var user: UserInfo = UserInfo(0,"","","null",null,null,true)
    var isNewChat = true
    var isMessageOver = false

    var messageList: List<MessageItem> = arrayListOf()
    fun getUserInfo(){
        _userLiveData.value = userLiveData.value?.copy(userInfo = user)
    }
    fun fetchMessages(list: List<MessageItem>){
        _chattingPageViewStateLiveData.value = ChattingFragmentPageViewState(user,list)
        if (!list.isNullOrEmpty()){
            isNewChat = false
        }
    }
    init {
        //getAllMessages()
        _messageFetchRequestLiveData.postValue(true)
        getMessagesFromPage(1)
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

    fun getMessagesFromPage(pageId: Int){
        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.getMessagesFromPage(user.uId.toString(),pageId).get()?.let {
                withContext(Dispatchers.Main){
                    if (it.isNullOrEmpty()){
                        _messageFetchRequestLiveData.postValue(false)
                        isMessageOver = true
                    }
                    messageList = it + messageList
                    fetchMessages(messageList)
                }
            }
        }

    }

    fun fetchMessagesOnSocket(args: Args){
        val dateNow = Date()
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = timeFormat.format(dateNow)

        if (user.uId == args.senderId.toInt()){

            val newMessage = MessageItem(args.message,args.senderId,args.receiverId,currentTime)

            Log.e("test","viewmodel çalıştı")
            viewModelScope.launch(Dispatchers.Main) {
                Log.e("weltestscope","Çaıştoı")
                messageList = messageList + newMessage

                fetchMessages(messageList)
            }
        }
    }

    fun getMessages(args: LiveData<Array<Any>>, context:Context){
        val dateNow = Date()
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = timeFormat.format(dateNow)

        val sharedPreferences = context.getSharedPreferences("LoggedUserID",Context.MODE_PRIVATE)
        val loggedUserId = sharedPreferences.getString("LoggedUserId","")

        if (user.uId == args.value?.get(0)){

            val newMessage = MessageItem(args.value?.get(1).toString(),args.value?.get(0).toString(),loggedUserId.toString(),currentTime)
            viewModelScope.launch(Dispatchers.Main) {

                messageList = messageList + newMessage

                fetchMessages(messageList)
            }
        }
        /*Socket.setSocket(context)
        val mSocket = Socket.getSocket()

        mSocket.connect()

        mSocket.on("message"){ args ->
            Log.e("socket","sockete girdi")
            if (args[0] != null){
                val message = mutableListOf<MessageItem>()
                Log.e("socketOn","${args[1]}")
                Log.e("gelenTarih","${args[2]}")
                val newMessage = MessageItem(args[1].toString(),args[0].toString(),loggedUserId.toString(),currentTime)

                if (user.uId == args[0]){

                    Log.e("test","çalıştı")
                    viewModelScope.launch(Dispatchers.Main) {
                        messageList = messageList + newMessage

                        fetchMessages(messageList)
                    }
                }
            }else{
                Log.e("socketOn","else düştü")
            }
        }*/

        /*mSocket.on("newMessage:user:${user.uId}"){ args ->
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
        }*/
    }
    fun sendMessage(Socket: SocketHandler,context: Context,message: String){
        val sharedPreferences = context.getSharedPreferences("LoggedUserID",Context.MODE_PRIVATE)
        val loggedUserId = sharedPreferences.getString("LoggedUserId","")

        val dateNow = Date()
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = timeFormat.format(dateNow)

        val mSocket = Socket.getSocket()

        if (message.isNotEmpty() && message.isNotBlank()){

            mSocket.emit("message",user.uId,message)
            val newMessage = MessageItem(message,loggedUserId.toString(),user.uId.toString(),currentTime.toString())
            messageList = messageList + newMessage

            fetchMessages(messageList)
        }else{
            _errorMessageLiveData.postValue("Lütfen Bir Mesaj Girin")
        }
    }
}