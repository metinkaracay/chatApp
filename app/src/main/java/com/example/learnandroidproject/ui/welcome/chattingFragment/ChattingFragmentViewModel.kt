package com.example.learnandroidproject.ui.welcome.chattingFragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.Args
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import com.github.michaelbull.result.get
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.client.Ack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
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

    private val _messageFetchRequestLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val messageFetchRequestLiveData: LiveData<Boolean> = _messageFetchRequestLiveData

    var user: UserInfo = UserInfo(0,"","","null",null,null,true)
    var isNewChat = true
    var isMessageOver = false
    var sendingMessage: MutableMap<String, MutableList<Args>> = mutableMapOf()

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
        _messageFetchRequestLiveData.postValue(true)
        getMessagesFromPage(1)
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

        Log.e("çökertti","userid : ${user.uId}")
        Log.e("çökertti","senderid : ${args.senderId}")
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
    fun sendMessage(Socket: SocketHandler,context: Context,message: String){
        val sharedPreferences = context.getSharedPreferences("LoggedUserID",Context.MODE_PRIVATE)
        val loggedUserId = sharedPreferences.getString("LoggedUserId","")

        val dateNow = Date()
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = timeFormat.format(dateNow)

        val mSocket = Socket.getSocket()

        if (message.isNotEmpty() && message.isNotBlank()){

            val messageJson = JSONObject()
            messageJson.put("senderId", user.uId)
            messageJson.put("message", message)

            mSocket.emit("message",messageJson.toString(), Ack{ args ->

                val ackReceiverId = args.getOrNull(0)
                val json = JSONObject(ackReceiverId.toString())

                val ackReceiver = json.get("receiverId")
                val ackMessageTime = json.get("payloadDate")

                var model = Args(message,loggedUserId.toString(),ackReceiver.toString(),ackMessageTime.toString(),true)

                // SendingMessage daha önce oluşmamışsa oluştur
                val messageListModel = sendingMessage.getOrPut(ackReceiver.toString()) { mutableListOf() }

                // Modeli liste içine ekle
                messageListModel.add(model)
                Log.e("gönderilen model","$sendingMessage")

            })
            val newMessage = MessageItem(message,loggedUserId.toString(),user.uId.toString(),currentTime.toString())
            messageList = messageList + newMessage
            fetchMessages(messageList)
        }else{
            _errorMessageLiveData.postValue("Lütfen Bir Mesaj Girin")
        }
    }
}