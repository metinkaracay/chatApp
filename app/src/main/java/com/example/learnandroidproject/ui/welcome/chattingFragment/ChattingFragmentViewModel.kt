package com.example.learnandroidproject.ui.welcome.chattingFragment

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.common.isSuccess
import com.example.learnandroidproject.common.util.DispatchGroup
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.Args
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem
import com.example.learnandroidproject.di.ChatDatabase
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
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
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import androidx.core.content.FileProvider
import java.io.IOException
import java.net.URL
import java.util.*
import javax.inject.Inject
@HiltViewModel
class ChattingFragmentViewModel @Inject constructor(private val datingApiRepository: DatingApiRepository): BaseViewModel(){

    private val _chattingPageViewStateLiveData: MutableLiveData<ChattingFragmentPageViewState> = MutableLiveData()
    val chattingPageViewStateLiveData: LiveData<ChattingFragmentPageViewState> = _chattingPageViewStateLiveData

    private val _errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val errorMessageLiveData: LiveData<String> = _errorMessageLiveData

    private val _messageFetchRequestLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val messageFetchRequestLiveData: LiveData<Boolean> = _messageFetchRequestLiveData

    private val _newMessageOnTheChatLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val newMessageOnTheChatLiveData: LiveData<Boolean> = _newMessageOnTheChatLiveData

    var user: UserInfo = UserInfo(0,"","","null",null,null,true) // Userchat ise buası dolar
    var pageId = 1
    var isNewChat = true
    //var isMessageOver = false // TODO bir yer patlamazsa sil boşa çıktı
    var sendingMessage: MutableMap<String, MutableList<Args>> = mutableMapOf()
    var messageList: List<MessageItem> = arrayListOf()
    var loggedUserId: String = "0"
    var lastMessageTime = 0L

    val dispatchGroup = DispatchGroup()
    fun sendMessagesToPageViewState(list: List<MessageItem>){
        viewModelScope.launch(Dispatchers.Main){
            _chattingPageViewStateLiveData.value = chattingPageViewStateLiveData.value?.copy(messages = list)
        }
        if (!list.isNullOrEmpty()){
            isNewChat = false
        }
    }

    fun getMessagesFromPage(sendTime: Long, context: Context){
        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.getMessagesFromPage(user.uId.toString(),pageId,sendTime).get()?.let {
                withContext(Dispatchers.Main){
                    if (it.isEmpty()){
                        getMessagesFromRoom(context)

                        //isMessageOver = true
                        _chattingPageViewStateLiveData.value = _chattingPageViewStateLiveData.value?.copy(isLoaded = true)
                    }else{
                        dispatchGroup.enter()
                        insertMessageToRoom(context,it,null)

                        dispatchGroup.notify { // Toplu kaydetme sırasında kaydetme işlemi tamamlandığında çalışır
                            // Her seferinde 10 tane mesaj geldiği için room ile backend'i eşitleyene kadar sürekli istek atıyoruz
                            getMessagesFromPage(it[it.size-1].messageTime.toLong(),context)

                        }
                    }
                }
            }
        }
    }
    fun getLastMessageFromRoom(context: Context){
        viewModelScope.launch(Dispatchers.IO){
            val dao = ChatDatabase.getInstance(context).MessageDao()
            val lastMessage = dao.filterMessages(user.uId.toString())

            if (lastMessage != null) {
                if (lastMessage.messageTime.isNullOrEmpty()) {
                    Log.e("getLastMessageFromRoom", "lastMessage boş")
                } else {
                    lastMessageTime = lastMessage.messageTime.toLong()
                    getMessagesFromPage(lastMessage.messageTime.toLong(), context)
                }
            } else {
                getMessagesFromPage(0,context)
                Log.e("getLastMessageFromRoom", "lastMessage null")
            }
        }
        viewModelScope.launch(Dispatchers.Main){
            _chattingPageViewStateLiveData.value = chattingPageViewStateLiveData.value?.copy(user, arrayListOf()) ?: ChattingFragmentPageViewState(user, arrayListOf())
        }
    }
    fun getMessagesFromRoom(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            val dao = ChatDatabase.getInstance(context).MessageDao()
            val newMessageList = dao.getAllMessages(loggedUserId.toInt(),user.uId,(pageId-1)*10)
            if (!newMessageList.isEmpty()){
                Log.e("getLastMessageFromRoom","mesajlar çekildi")
                messageList = newMessageList.reversed() + messageList
                sendMessagesToPageViewState(messageList)
                _messageFetchRequestLiveData.postValue(true)
            }else{
                _messageFetchRequestLiveData.postValue(false)
                isNewChat = false
            }
            pageId++
        }
    }
    fun downloadImageAndConvertToUri(imageUrl: String, context: Context): Uri? {
        try {
            val inputStream = URL(imageUrl).openStream()
            val imageBitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            Log.e("gelennnnnnnnn","$imageBitmap , $imageUrl")
            val uri = getImageUriFromBitmap(context, imageBitmap)
            return uri
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
        /*val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        Log.e("gelennnnnnnnn1","$bitmap")
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        Log.e("gelennnnnnnnn2","$path")
        return Uri.parse(path)*/
        val uuid = UUID.randomUUID()

        val imageDir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "chatApp")
        if (!imageDir.exists()) {
            imageDir.mkdirs()
        }
        val imageFile = File(imageDir, "$uuid.jpg")

        val outputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.close()

        // Uri'yi FileProvider ile oluştur
        return FileProvider.getUriForFile(context, context.packageName + ".provider", imageFile)

    }
    fun fetchMessagesOnSocket(args: Args,context: Context){
        if (user.uId == args.senderId.toInt()){
            val newMessage = MessageItem(args.message,args.messageType,args.senderId,args.receiverId,args.messageTime)
            viewModelScope.launch(Dispatchers.IO){
                if (args.messageType == "image"){
                    viewModelScope.launch(Dispatchers.IO){
                        val imageUri = downloadImageAndConvertToUri(args.message,context)
                        newMessage.messageTime = args.messageTime // Bu satır olmayınca sebepsiz bir şekilde messageTime değişiyor. 11:51 gibi bir saate dönüşüyor. Bunu fixlemek için
                        if (imageUri != null){
                            newMessage.message = imageUri.toString()
                            insertMessageToRoom(context,null,newMessage)
                        }else {
                            _errorMessageLiveData.postValue("Resim Yüklenemedi Daha Sonra Tekrar Deneyin")
                            Log.e("ImageUriError", "Resim Uri'si null")
                        }
                    }
                }else{
                    insertMessageToRoom(context,null,newMessage)
                }
            }
            viewModelScope.launch(Dispatchers.Main) {
                _newMessageOnTheChatLiveData.postValue(true)
                messageList = messageList + newMessage
                sendMessagesToPageViewState(messageList)
            }
        }
    }
    fun sendMessage(context: Context,message: String, messageType: String,imageUri: Uri?){
        val sharedPreferences = context.getSharedPreferences("LoggedUserID",Context.MODE_PRIVATE)
        val loggedUserId = sharedPreferences.getString("LoggedUserId","")

        val dateNow = Date()
        var unixTimestamp = dateNow.time // Zaman Unix olarak alınır

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

                if (messageType == "image"){
                    Log.e("imageeee","$imageUri")
                    var newImageMessage = messageItem
                    newImageMessage.message = imageUri.toString()
                    insertMessageToRoom(context,null,newImageMessage)
                }else{
                    insertMessageToRoom(context,null,messageItem)
                }

                // SendingMessage daha önce oluşmamışsa oluştur
                val messageListModel = sendingMessage.getOrPut(ackReceiver.toString()) { mutableListOf() }

                // Modeli liste içine ekle
                messageListModel.add(model)

            })
            Log.e("gönderilenDateeee","$unixTimestamp")
            val newMessage = MessageItem(message,messageType,loggedUserId.toString(),user.uId.toString(),unixTimestamp.toString())
            messageList = messageList + newMessage
            sendMessagesToPageViewState(messageList)
            _newMessageOnTheChatLiveData.postValue(false) // TODO trueydu
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
                            Log.e("imageURL", "$imageStream")

                            /*// Fotoğrafı özel dizine kaydet ve URI'sini al
                            val savedImageUri = saveImageToPrivateDirectory(BitmapFactory.decodeStream(imageStream), "${uuid}.jpg", context)

                            if (savedImageUri != null) {
                                // Fotoğrafı özel dizine kaydettikten sonra gönder
                                sendMessage(context, url, "image", savedImageUri)
                            } else {
                                Log.e("ImageSaveError", "Fotoğraf özel dizine kaydedilemedi")
                            }*/

                            // Chatte gözükmesi için backend'ten alınan urli sockete emitler
                            sendMessage(context,url,"image",selectedImage)
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
    fun insertMessageToRoom(context: Context, messages: List<MessageItem>?, message: MessageItem?){
        viewModelScope.launch(Dispatchers.IO) {
            val messageDao = ChatDatabase.getInstance(context).MessageDao()
            if (message == null && messages != null){
                // Chatte değilken gelen mesajları backendden alırken liste halinde geldiği için tüm mesajlara bakıp image olanları telefona indirip uri'ını room'a kaydediyoruz
                for (i in 0 until messages.size){
                    if (messages[i].messageType == "image"){
                        Log.e("insertMEssage2","${messages[i].message}")
                        val imageUri = downloadImageAndConvertToUri(messages[i].message,context)
                        if (imageUri != null){
                            messages[i].message = imageUri.toString()
                        }
                    }
                }
                messageDao.insertAllMessages(messages)
                dispatchGroup.leave()
            }else if (messages == null && message != null){
                Log.e("insertMEssage3","${message}")
                messageDao.insertMessage(message)
            }
        }
    }
}