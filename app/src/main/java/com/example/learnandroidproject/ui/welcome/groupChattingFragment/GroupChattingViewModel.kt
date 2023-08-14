package com.example.learnandroidproject.ui.welcome.groupChattingFragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.Args
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import com.example.learnandroidproject.ui.welcome.chattingFragment.ChattingFragmentPageViewState
import com.example.learnandroidproject.ui.welcome.chattingFragment.SocketHandler
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
class GroupChattingViewModel @Inject constructor(private val datingApiRepository: DatingApiRepository) : BaseViewModel() {

    private val _groupChattingPageViewStateLiveData: MutableLiveData<GroupChattingPageViewState> = MutableLiveData()
    val groupChattingPageViewStateLiveData: LiveData<GroupChattingPageViewState> = _groupChattingPageViewStateLiveData

    private val _newMessageOnTheChatLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val newMessageOnTheChatLiveData: LiveData<Boolean> = _newMessageOnTheChatLiveData

    private val _messageFetchRequestLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val messageFetchRequestLiveData: LiveData<Boolean> = _messageFetchRequestLiveData

    private val _errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val errorMessageLiveData: LiveData<String> = _errorMessageLiveData

    var messageList: List<MessageItem> = arrayListOf()
    var group: GroupInfo = GroupInfo(0,"","","null","null",false) // TODO seeni düzeltmen gerekebilir
    private var pageId = 1
    var isNewChat = true
    var fetchSocketData = false // mesajları biriktirdiği için chat'e ilk girdiğinde son mesajı birkaç kez yazdırıyordu. Onu düzeltmek için kontrol
    var sendingMessage: MutableMap<String, MutableList<Args>> = mutableMapOf()

    init {
        _messageFetchRequestLiveData.postValue(true)
        fetchMessages()
    }

    fun fetchMessages(){
        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.getGroupMessagesFromPage(group.groupId.toString(),pageId).get()?.let {
                withContext(Dispatchers.Main){
                    _groupChattingPageViewStateLiveData.value = GroupChattingPageViewState(group,it)
                    messageList = it + messageList
                    pageId++
                    fetchSocketData = true
                }
            }
        }
    }

    fun sendMessagesToPageViewState(list: List<MessageItem>){
        _groupChattingPageViewStateLiveData.value = GroupChattingPageViewState(group,list)
        if (!list.isNullOrEmpty()){
            isNewChat = false
        }
    }

    fun fetchMessagesOnSocket(args: Args){
        val dateNow = Date()
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = timeFormat.format(dateNow)

        Log.e("mesaj geldi1","gelen son mesaj ${args}")
        if (group.groupId == args.receiverId.toInt() && fetchSocketData){


            val newMessage = MessageItem(args.message,args.senderId,args.receiverId,currentTime)

            viewModelScope.launch(Dispatchers.Main) {
                Log.e("mesaj geldi2","gelen son mesaj ${args}")

                _newMessageOnTheChatLiveData.postValue(true)
                messageList = messageList + newMessage
                sendMessagesToPageViewState(messageList)
            }
        }
        fetchSocketData = true
        _newMessageOnTheChatLiveData.postValue(false)
    }

    fun sendMessage(Socket: SocketHandler, context: Context, message: String){
        val sharedPreferences = context.getSharedPreferences("LoggedUserID", Context.MODE_PRIVATE)
        val loggedUserId = sharedPreferences.getString("LoggedUserId","")

        val mSocket = Socket.getSocket()

        if (message.isNotEmpty() && message.isNotBlank()){

            val messageJson = JSONObject()
            messageJson.put("receiverId", group.groupId)
            messageJson.put("message", message)

            mSocket.emit("message:group",messageJson.toString(), Ack{ args ->

                val ackReceiverId = args.getOrNull(0)
                val json = JSONObject(ackReceiverId.toString())

                val ackReceiver = json.get("receiverId")
                val ackMessageTime = json.get("payloadDate")

                var model = Args(message,loggedUserId.toString(),ackReceiver.toString(),ackMessageTime.toString(),true)

                // SendingMessage daha önce oluşmamışsa oluştur
                val messageListModel = sendingMessage.getOrPut(ackReceiver.toString()) { mutableListOf() }

                // Modeli liste içine ekle
                messageListModel.add(model)
                Log.e("gönderilen grup modeli","$sendingMessage")
                _newMessageOnTheChatLiveData.postValue(true)

            })
            fetchSocketData = true
            _newMessageOnTheChatLiveData.postValue(false)
        }else{
            _errorMessageLiveData.postValue("Lütfen Bir Mesaj Girin")
        }
    }
}