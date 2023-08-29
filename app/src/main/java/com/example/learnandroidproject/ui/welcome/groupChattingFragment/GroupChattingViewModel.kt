package com.example.learnandroidproject.ui.welcome.groupChattingFragment

import android.content.Context
import android.util.Log
import androidx.cardview.widget.CardView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.common.SingleLiveEvent
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.Args
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.RaceData
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.UserRaceDatas
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupMember
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import com.example.learnandroidproject.ui.welcome.chattingFragment.SocketHandler
import com.github.michaelbull.result.get
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.client.Ack
import kotlinx.coroutines.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class GroupChattingViewModel @Inject constructor(private val datingApiRepository: DatingApiRepository) : BaseViewModel() {

    private val _groupChattingPageViewStateLiveData: MutableLiveData<GroupChattingPageViewState> = MutableLiveData()
    val groupChattingPageViewStateLiveData: LiveData<GroupChattingPageViewState> = _groupChattingPageViewStateLiveData

    private val _newMessageOnTheChatLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val newMessageOnTheChatLiveData: LiveData<Boolean> = _newMessageOnTheChatLiveData

    private val _messageFetchRequestLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val messageFetchRequestLiveData: LiveData<Boolean> = _messageFetchRequestLiveData

    private val _positionPercentsCalculatedLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val positionPercentsCalculatedLiveData: LiveData<Boolean> = _positionPercentsCalculatedLiveData

    private val _errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val errorMessageLiveData: LiveData<String> = _errorMessageLiveData

    private val _eventStateLiveData: SingleLiveEvent<MutableMap<Int,Int>> = SingleLiveEvent()
    val eventStateLiveData: LiveData<MutableMap<Int,Int>> = _eventStateLiveData

    var messageList: List<MessageItem> = arrayListOf()
    var group: GroupInfo = GroupInfo(0,"","","null","null",true, false) // TODO seeni düzeltmen gerekebilir
    private var pageId = 1
    var isNewChat = true
    var fetchSocketData = false // mesajları biriktirdiği için chat'e ilk girdiğinde son mesajı birkaç kez yazdırıyordu. Onu düzeltmek için kontrol
    var sendingMessage: MutableMap<String, MutableList<Args>> = mutableMapOf()
    var senderUserName: MutableMap<Int, String> = mutableMapOf()
    var members: List<GroupMember> = arrayListOf()
    var loggedUserid = 0
    var raceStatus = 0

    var isAdmin = false
    var isRaceStart = false

    // Yarışta sıralama hesaplamak ve animasyonu oynatabilmek için gerken değerler
    var userImageViews: List<CardView> = arrayListOf() // Eski sil
    var frameLayoutWidth = 0
    private var userPoints: MutableMap<Int, Int> = mutableMapOf() // TODO eski
    val userRaceDatas: MutableList<UserRaceDatas> = mutableListOf()
    var users: List<Int> = mutableListOf(0,0,0)
    var userPositionPercentages = mutableListOf<Float>()

    // sayaç
    private var countdownJob: Job? = null

    // Yarış sırasında odada olmayan biri daha sonradan odaya girdiğinde yarışı görebilmesi için gereken veriler
    var raceDatas: List<RaceData> = arrayListOf()

    fun fetchMessages(context: Context){
        val sharedPreferences = context.getSharedPreferences("LoggedUserID", Context.MODE_PRIVATE)
        val loggedUserId = sharedPreferences.getString("LoggedUserId","")

        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.getGroupMessagesFromPage(group.groupId.toString(),pageId).get()?.let {
                withContext(Dispatchers.Main){
                    val messages = it.messageList
                    members = it.userList
                    raceDatas = it.raceState
                    val raceRemainingTime = it.remainingTime

                    for (i in 0 until members.size){
                        if (members[i].uRole == "Admin"){
                            if (loggedUserId!!.toInt() == members[i].uId){
                                isAdmin = true
                            }
                        }
                    }

                    // Yarış varken odaya giren kullanıcıların yarışı izleyebilmesini sağlar
                    if (raceDatas.isNotEmpty() && raceRemainingTime > 0){
                        setRaceState(true)
                        startCountdown(raceRemainingTime.toString())
                        setupUsersForSpectator(raceDatas)
                        Log.e("raceDatas21","$raceDatas")
                    }else if (raceRemainingTime > 0){
                        setRaceState(true)
                        startCountdown(raceRemainingTime.toString())
                    }


                    messageList = messages + messageList
                    fetchSocketData = true
                    _messageFetchRequestLiveData.postValue(true)
                    if (pageId == 1){
                        setMembersNameById()
                        _groupChattingPageViewStateLiveData.value = GroupChattingPageViewState(group,messages,isAdmin,isRaceStart, isLoaded = true,membersNameList = senderUserName)
                    }else{
                        _groupChattingPageViewStateLiveData.value = groupChattingPageViewStateLiveData.value?.copy(messages = messageList)
                        if (messages.isNullOrEmpty()){ // Son mesaj çekildikten sonra istek atmasını engeller
                            isNewChat = false
                        }
                    }
                    pageId++
                }
            }
        }
    }

    fun setMembersNameById(){
        senderUserName.clear()

        for (member in members) {
            senderUserName[member.uId] = member.uName ?: ""
        }
    }

    fun sendMessagesToPageViewState(list: List<MessageItem>){
        _groupChattingPageViewStateLiveData.value = groupChattingPageViewStateLiveData.value?.copy(messages = list)
    }

    fun fetchMessagesOnSocket(args: Args){
        val dateNow = Date()
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = timeFormat.format(dateNow)

        if (group.groupId == args.receiverId.toInt() && fetchSocketData){


            val newMessage = MessageItem(args.message,args.senderId,args.receiverId,currentTime)

            viewModelScope.launch(Dispatchers.Main) {

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
        loggedUserid = loggedUserId!!.toInt()

        val mSocket = Socket.getSocket()

        if (message.isNotEmpty() && message.isNotBlank()){

            val messageJson = JSONObject()
            Log.e("receiverId","${group.groupId}")
            messageJson.put("receiverId", group.groupId)
            messageJson.put("message", message)

            mSocket.emit("message:group",messageJson.toString(), Ack{ args ->

                val ackReceiverId = args.getOrNull(0)
                val json = JSONObject(ackReceiverId.toString())

                val ackReceiver = json.get("receiverId")
                val ackMessageTime = json.get("payloadDate")
                val ackUserItemCount = json.get("itemCount")

                Log.e("gelenack","$ackUserItemCount")

                val model = Args(message,loggedUserId.toString(),ackReceiver.toString(),ackMessageTime.toString(),true)

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

    fun startToRace(Socket: SocketHandler, remainingTime: String){
        val mSocket = Socket.getSocket()

        val json = JSONObject()
        json.put("groupId", group.groupId)
        json.put("seconds", remainingTime.toInt())
        json.put("status", raceStatus)

        Log.e("gönderilen json","$json")
        mSocket.emit("event:status",json.toString(), Ack { args ->

            val ackReceiverId = args.getOrNull(0)
            val json = JSONObject(ackReceiverId.toString())

            val ackReceiver = json.get("status")

            if (ackReceiver == 0 && members.size > 3){
                setRaceState(true)
                startCountdown(remainingTime)
                var model: MutableMap<Int,Int> = mutableMapOf()
                model[group.groupId] = 0
                _eventStateLiveData.postValue(model)
            }else if (members.size <= 3){
                _errorMessageLiveData.postValue("Yarış başlatabilmek için siz hariç en az 3 üye daha olmalı")
            }else{
                _errorMessageLiveData.postValue("Şu anda yarış başlatamazsınız")
            }
        })
    }

    fun setRaceState(state: Boolean){
        isRaceStart = state
        viewModelScope.launch(Dispatchers.Main){
            _groupChattingPageViewStateLiveData.value = groupChattingPageViewStateLiveData.value?.copy(isRaceStart = isRaceStart)
        }
        setupUsers()
    }

    fun setTimerPopUp(state: Boolean){
        Log.e("gelen race state","$state")
        _groupChattingPageViewStateLiveData.value = groupChattingPageViewStateLiveData.value?.copy(popUpVisibility = state)
    }

    fun setupUsers() {
        userPoints.clear()
        members.filter { it.uRole == "User" }.forEachIndexed { index, user ->
            userPoints[user.uId] = 0
        }
        Log.e("race_users","$userPoints") // TODO burayı dene
    }
    fun setupUsersForSpectator(dataset: List<RaceData>) {
        userPoints.clear()
        userPoints.values.clear()

        Log.e("gelen_dataset","${dataset[0].userId}")

        for(i in 0 until dataset.size ){
            val newUser = UserRaceDatas(dataset[i].userId, dataset[i].point, dataset[i].carId, 0.0f)
            userRaceDatas.add(newUser)
        }

        Log.e("percentSon","${userRaceDatas}")

        for (i in 0 until raceDatas.size){ // TODO sil
            Log.e("percentÖn","puan : ${userRaceDatas[i].point}, id: ${userRaceDatas[i].userId}, perc: ${userRaceDatas[i].racePercent}")
        }
        Log.e("userRaceDATAAAASpec","${userRaceDatas.size}")
        updateUsersPosition()
    }

    fun updateUserPoints(args: List<RaceData>) {
        // Admin koptu yarışı bitir
        if(args[0].userId == -1 && args[0].point == -1){
            userPositionPercentages.clear()
            cancelCountdown()
        }else if (args[0].userId == 0){
            setRaceState(true)
            startCountdown(args[0].point.toString())
        }else{
            Log.e("testArgs_sil","${args}")
            for (i in 0 until args.size){
                val userId = args[i].userId
                val point = args[i].point
                if (!userRaceDatas.isNullOrEmpty()){
                    Log.e("userRaceDataKontrol","${userRaceDatas[0].userId}")
                }
                // Gelen datadaki kişi ilk üçte var mı kontrol et
                val existingUser = userRaceDatas.find { it.userId == userId }

                if (existingUser != null) { // Varsa çalışır
                    Log.e("userRaceDataKontr","if çalıştı $userId")
                    existingUser.point = point
                    existingUser.carId = args[i].carId
                } else { // Yoksa çalışır
                    Log.e("userRaceKontr","else çalıştı $userId")
                    val newUser = UserRaceDatas(userId, point, args[i].carId, 0.0f)
                    userRaceDatas.add(newUser)
                }
            }

            // ilk mesaj atıldığında 1 veri geliyor. Bize 3 kişi lazım olduğu için 2 tane boş hesap ekler
            if (userRaceDatas.size < 3){
                for (i in 0 until 3 - userRaceDatas.size){
                    val emptyUser = UserRaceDatas(0,0,0, 0.0f)// Todo Boş user oluştururken verdiğim datalar patlatabilir
                    userRaceDatas.add(emptyUser)

                    Log.e("userRaceDataForsayaç","${i+1}")
                }
            }else if (userRaceDatas.size > 3){
                val lowestPoint = userRaceDatas.minByOrNull { it.point }
                Log.e("userRaceDataForelse","silindi : ${lowestPoint?.userId}")

                if (lowestPoint != null){
                    userRaceDatas.remove(lowestPoint)
                    for (i in 0 until userRaceDatas.size){ // TODO top 3teki kişileri yazdırır işin bitince sil algoritmayı etkilemiyor
                        Log.e("userRaceDatakişiler","${userRaceDatas[i].userId}")
                    }
                }
            }
            Log.e("userRaceDATAAAASock","${userRaceDatas.size}")
            updateUsersPosition()
        }
    }

    private fun updateUsersPosition() {
        userPositionPercentages.clear()
        var userPhotoList = mutableListOf("","","","")
        val userPointList = mutableListOf("","","","")

        // Yarışçının fotoğrafını ve puanını ekler
        for (userId in 0 until userRaceDatas.size) {
            val user = members.find { it.uId == userRaceDatas[userId].userId }
            val carId = userRaceDatas[userId].carId
            val photoUrl = user?.uPhoto ?: "null"
            val point = userRaceDatas[userId].point.toString()
            userPhotoList[carId] = photoUrl
            userPointList[carId] = point
        }

        val totalPoint = userRaceDatas.sumOf { it.point }
        Log.e("racePercenttt","$totalPoint")

        // Yarışçının yüzdesel pozisyonunu hesaplar
        for (i in 0 until userRaceDatas.size){ // Tanımsız değil
            Log.e("racePercenttt2","${userRaceDatas[i].point.toFloat()}")
            val positionPercent = if (userRaceDatas[i].point.toFloat() != 0.0f){
                userRaceDatas[i].point.toFloat() / totalPoint
            }else{ // Tanımsız
                0.0f
            }
            Log.e("racePercenttt3","$positionPercent")
            userRaceDatas[i].racePercent = positionPercent
        }

        _positionPercentsCalculatedLiveData.postValue(true)

        viewModelScope.launch(Dispatchers.Main){
            _groupChattingPageViewStateLiveData.value = groupChattingPageViewStateLiveData.value?.copy(userPhoto = userPhotoList.toList(), userPoints = userPointList.toList())
        }
    }

    fun startCountdown(remainingTime: String) {
        countdownJob?.cancel()

        countdownJob = viewModelScope.launch(Dispatchers.IO) {
            for (i in remainingTime.toInt() downTo 0) {
                val minute = i / 60
                val remainingSeconds = i % 60
                val formattedTime = String.format("%02d:%02d", minute, remainingSeconds)
                withContext(Dispatchers.Main) {
                    _groupChattingPageViewStateLiveData.value = groupChattingPageViewStateLiveData.value?.copy(remainingTime = formattedTime)
                }
                delay(1000)
            }
        }
    }
    fun cancelCountdown() {
        // Yarış bittiğinde eldeki verileri sıfırlar
        userPoints.clear()
        userPoints.values.clear()
        userPositionPercentages.clear()
        userRaceDatas.clear()
        for (card in userImageViews){
            card.x = -190f // Userların cardlarını en başa çeker
        }
        countdownJob?.cancel()
        viewModelScope.launch(Dispatchers.Main){
            _groupChattingPageViewStateLiveData.value = groupChattingPageViewStateLiveData.value?.copy(isRaceStart = false)
        }
    }

    fun checkRemainingTime(remainingTime: String): Boolean{
        if (remainingTime.isNullOrEmpty() || remainingTime.toInt() == 0){
            _errorMessageLiveData.postValue("Süre Belirlemediniz")
            return false
        }else if(remainingTime.toInt() > 301){
            _errorMessageLiveData.postValue("Etkinlik 5 dakikadan fazla süremez")
            return false
        }
        return true
    }
}