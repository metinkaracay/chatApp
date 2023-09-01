package com.example.learnandroidproject.ui.welcome.chattingFragment

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.common.isSuccess
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.Args
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem
import com.example.learnandroidproject.di.ChatDatabase
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import com.example.learnandroidproject.ui.welcome.groupChattingFragment.GroupChattingPageViewState
import com.github.michaelbull.result.get
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.client.Ack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
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

    private val _newMessageOnTheChatLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val newMessageOnTheChatLiveData: LiveData<Boolean> = _newMessageOnTheChatLiveData

    var user: UserInfo = UserInfo(0,"","","null",null,null,true) // Userchat ise buası dolar
    var pageId = 1
    var isNewChat = true
    var isMessageOver = false
    var sendingMessage: MutableMap<String, MutableList<Args>> = mutableMapOf()
    var fetchSocketData = false
    var messageList: List<MessageItem> = arrayListOf()
    fun getRoomInfo(){
        _userLiveData.value = userLiveData.value?.copy(userInfo = user)
    }
    fun sendMessagesToPageViewState(list: List<MessageItem>){
        viewModelScope.launch(Dispatchers.Main){
            _chattingPageViewStateLiveData.value = ChattingFragmentPageViewState(user,list)
        }
        if (!list.isNullOrEmpty()){
            isNewChat = false
        }
    }
    init {
        _messageFetchRequestLiveData.postValue(true)
        getMessagesFromPage()
    }

    fun getMessagesFromPage(){
        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.getMessagesFromPage(user.uId.toString(),pageId).get()?.let {
                withContext(Dispatchers.Main){
                    if (it.isNullOrEmpty()){
                        _messageFetchRequestLiveData.postValue(false)
                        isMessageOver = true
                    }
                    messageList = it + messageList
                    sendMessagesToPageViewState(messageList)
                    pageId++
                }
            }
        }
    }

    fun getLastMessageFromRoom(context: Context){
        viewModelScope.launch(Dispatchers.IO){
            val dao = ChatDatabase.getInstance(context).MessageDao()
            val lastMessage  = dao.filterMessages(user.uId.toString())
            Log.e("lasssssst","$lastMessage")
        }
    }

    fun fetchMessagesOnSocket(args: Args,context: Context){
        val dateNow = Date()
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = timeFormat.format(dateNow)

        if (user.uId == args.senderId.toInt() && fetchSocketData){

            val newMessage = MessageItem(args.message,args.messageType,args.senderId,args.receiverId,currentTime)
            var newMessageForRoom = newMessage
            newMessageForRoom.messageTime = args.messageTime
            insertMessageToRoom(context,newMessageForRoom)

            viewModelScope.launch(Dispatchers.Main) {
                _newMessageOnTheChatLiveData.postValue(true)
                messageList = messageList + newMessage
                sendMessagesToPageViewState(messageList)
            }
        }
        fetchSocketData = true // Chatte değilken gelen mesajları biriktirdiği için fazladan mesaj yazdırıyordu. Bu durumu engellemek için kontrol
        _newMessageOnTheChatLiveData.postValue(false)
    }
    fun sendMessage(context: Context,message: String, messageType: String){
        val sharedPreferences = context.getSharedPreferences("LoggedUserID",Context.MODE_PRIVATE)
        val loggedUserId = sharedPreferences.getString("LoggedUserId","")

        val dateNow = Date()
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = timeFormat.format(dateNow)

        val socket = SocketHandler
        val mSocket = socket.getSocket()

        if (message.isNotEmpty() && message.isNotBlank()){

            val messageJson = JSONObject()
            messageJson.put("receiverId", user.uId)
            messageJson.put("message", message)
            messageJson.put("type", messageType)

            mSocket.emit("message",messageJson.toString(), Ack{ args ->

                val ackReceiverId = args.getOrNull(0)
                val json = JSONObject(ackReceiverId.toString())

                val ackReceiver = json.get("receiverId").toString()
                val ackMessageTime = json.get("payloadDate").toString()

                var model = Args(message,loggedUserId.toString(),ackReceiver,ackMessageTime,messageType,true)
                val messageItem = MessageItem(message,messageType,loggedUserId.toString(),ackReceiver,ackMessageTime)

                // SendingMessage daha önce oluşmamışsa oluştur
                val messageListModel = sendingMessage.getOrPut(ackReceiver.toString()) { mutableListOf() }

                // Modeli liste içine ekle
                messageListModel.add(model)
                insertMessageToRoom(context,messageItem)
                Log.e("gönderilen model","$sendingMessage")
                _newMessageOnTheChatLiveData.postValue(true)

            })
            val newMessage = MessageItem(message,messageType,loggedUserId.toString(),user.uId.toString(),currentTime.toString())
            messageList = messageList + newMessage
            sendMessagesToPageViewState(messageList)
            _newMessageOnTheChatLiveData.postValue(false)
        }else{
            _errorMessageLiveData.postValue("Lütfen Bir Mesaj Girin")
        }
    }
    fun sendPhoto(selectedImage: Uri, context: Context){
        val uuid = UUID.randomUUID()

        viewModelScope.launch(Dispatchers.IO) {
            selectedImage?.let { imageUri ->
                val imageStream = context.contentResolver.openInputStream(imageUri)
                imageStream?.use {
                    val byteArray = it.readBytes()
                    val imageBody = byteArray.toRequestBody("image/*".toMediaTypeOrNull())
                    val imagePart = MultipartBody.Part.createFormData("image", "${uuid}.jpg", imageBody)

                    // Yükleme işlemini gerçekleştir
                    val uploadResult = datingApiRepository.friendChatSendPhoto(user.uId.toString(),imagePart)

                    if (uploadResult.isSuccess()) {
                        val responseString = uploadResult.component1()?.string() ?: ""
                        Log.e("responseString", responseString)
                        try {
                            val jsonObject = JSONObject(responseString)
                            val url = jsonObject.optString("url", "")
                            Log.e("imageURL", url)
                            // Chatte gözükmesi için backend'ten alınan urli sockete emitler
                            sendMessage(context,url,"image")
                        } catch (e: JSONException) {
                            Log.e("JSONParsingError", "Error parsing response JSON")
                        }
                    } else {
                        Log.e("yükleme durumu", "başarısız")
                    }
                }
            }
        }
    }
    fun insertMessageToRoom(context: Context, message: MessageItem){
        viewModelScope.launch(Dispatchers.IO) {
            val messageDao = ChatDatabase.getInstance(context).MessageDao()
            messageDao.insertMessage(message)
        }
    }
}