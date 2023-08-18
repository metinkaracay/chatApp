package com.example.learnandroidproject.ui.welcome.groupChattingFragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.common.SingleLiveEvent
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.Args
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.RaceData
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupMember
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import com.example.learnandroidproject.ui.welcome.chattingFragment.ChattingFragmentPageViewState
import com.example.learnandroidproject.ui.welcome.chattingFragment.SocketHandler
import com.github.michaelbull.result.get
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import io.socket.client.Ack
import kotlinx.coroutines.*
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
    var members: List<GroupMember> = arrayListOf()
    var loggedUserid = 0
    var raceStatus = 0

    var isAdmin = false
    var isRaceStart = false

    // Yarışta sıralama hesaplamak ve animasyonu oynatabilmek için gerken değerler
    var userImageViews: List<CardView> = arrayListOf()
    var frameLayoutWidth = 0
    private var userPoints: MutableMap<Int, Int> = mutableMapOf()
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
                    if (raceDatas.isNotEmpty()){
                        setRaceState(true)
                        startCountdown(raceRemainingTime.toString())
                        setupUsersForSpectator(raceDatas)
                        Log.e("raceDatas","$raceDatas")
                    }else{
                        setRaceState(true)
                        startCountdown(raceRemainingTime.toString())
                    }


                    messageList = messages + messageList
                    fetchSocketData = true
                    _messageFetchRequestLiveData.postValue(true)
                    if (pageId == 1){
                        _groupChattingPageViewStateLiveData.value = GroupChattingPageViewState(group,messages,isAdmin,isRaceStart, isLoaded = true)
                    }else{
                        _groupChattingPageViewStateLiveData.value = groupChattingPageViewStateLiveData.value?.copy(messages = messageList)
                    }
                    pageId++
                }
            }
        }
    }

    fun sendMessagesToPageViewState(list: List<MessageItem>){
        _groupChattingPageViewStateLiveData.value = groupChattingPageViewStateLiveData.value?.copy(messages = list)
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
        loggedUserid = loggedUserId!!.toInt()

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

        val membersWithoutAdmin = members.filter { it.uRole == "User" }
        val datasetUserIds = dataset.map { it.userId }.toSet()
        for (user in membersWithoutAdmin) {
            val userId = user.uId
            val point = if (datasetUserIds.contains(userId)) {
                dataset.find { it.userId == userId }?.point ?: 0
            } else {
                0
            }
            userPoints[userId] = point
        }
        Log.e("race_users2","$userPoints")

        Log.e("race_puanlar","${userPoints.values.toList()}")

        val sortedUserPoints = userPoints.entries.sortedByDescending { it.value }
        Log.e("sorted","$sortedUserPoints")

        // En yüksek puana sahip ilk 3 kişiyi alıyor
        val topThreeUsers = sortedUserPoints.take(3)

        val topThreeUserIds = topThreeUsers.map { it.key }
        Log.e("top_three_users", "$topThreeUserIds")
        updateUsersPosition(topThreeUserIds)
    }

    fun updateUserPoints(args: RaceData) {
        Log.e("race_args","$args")
        val userId = args.userId
        val point = args.point

        // Admin koptu yarışı bitir
        if(args.userId == -1 && args.point == -1){
            userPositionPercentages.clear()
            cancelCountdown()
            _groupChattingPageViewStateLiveData.value = groupChattingPageViewStateLiveData.value?.copy(isRaceStart = false)
        }else if (args.userId == 0){
            setRaceState(true)
            startCountdown(args.point.toString())
        }

        if (userPoints.containsKey(userId)) {
            Log.e("userId","$userId")
            Log.e("userId","$point")
            userPoints[userId] = point
        }
        Log.e("race_puanlar","${userPoints.values.toList()}")

        val sortedUserPoints = userPoints.entries.sortedByDescending { it.value }

        // En yüksek puana sahip ilk 3 kişiyi alıyor
        val topThreeUsers = sortedUserPoints.take(3)

        val topThreeUserIds = topThreeUsers.map { it.key }
        Log.e("top_three_users", "$topThreeUserIds")
        updateUsersPosition(topThreeUserIds)
    }

    private fun updateUsersPosition(topThreeUserIds: List<Int>) {
        var totalPoint = 0
        var loggedId = 0
        userPositionPercentages.clear()
        var userPhotoList = mutableListOf<String>()

        for (i in 0 until topThreeUserIds.size){
            Log.e("race_total_öncesi","gelen : ${userPoints[topThreeUserIds[i]]}")
            totalPoint += userPoints[topThreeUserIds[i]] ?: 0
            if (loggedUserid == topThreeUserIds[i]){
                loggedId = i+1
            }
        }
        viewModelScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){

                _groupChattingPageViewStateLiveData.value = groupChattingPageViewStateLiveData.value?.copy(loggedUserRank = loggedId)
            }
        }

        viewModelScope.launch(Dispatchers.Main){
            _groupChattingPageViewStateLiveData.value = groupChattingPageViewStateLiveData.value?.copy(loggedUserRank = loggedId)
        }

        for (userId in topThreeUserIds) {
            val user = members.find { it.uId == userId }
            val photoUrl = user?.uPhoto ?: "null"
            userPhotoList.add(photoUrl)
        }

        for (userId in topThreeUserIds) {
            val positionPercent = if (userPoints[userId]!!.toFloat() != 0.0f){
                Log.e("mat kontrol","tanımsız değil")
                userPoints[userId]!!.toFloat() / totalPoint
            }else{
                Log.e("mat kontrol","tanımsız")
                0.0f
            }
            userPositionPercentages.add(positionPercent)
            Log.e("race_user_percent","user: $userId,percent = $userPositionPercentages")
        }

        _positionPercentsCalculatedLiveData.postValue(true)

        viewModelScope.launch(Dispatchers.Main){
            _groupChattingPageViewStateLiveData.value = groupChattingPageViewStateLiveData.value?.copy(userPhoto = userPhotoList.toList())
        }
    }

    fun startCountdown(remainingTime: String) {
        countdownJob?.cancel()

        countdownJob = viewModelScope.launch(Dispatchers.IO) {
            for (i in remainingTime.toInt() downTo 0) {
                val minute = i / 60
                val remainingSeconds = i % 60
                val formattedTime = String.format("%02d:%02d", minute, remainingSeconds)
                Log.e("Süreee","$minute:$remainingSeconds")
                withContext(Dispatchers.Main) {
                    _groupChattingPageViewStateLiveData.value = groupChattingPageViewStateLiveData.value?.copy(remainingTime = formattedTime)
                }
                if (i == 0 ){
                    // Yeni yarışma açmadan önce eldeki verileri sıfırlar
                    //userPoints.clear()
                    userPositionPercentages.clear()
                    for (card in userImageViews){
                        card.x = 0.0f // Userların cardlarını en başa çeker
                    }
                    cancelCountdown()
                }
                delay(1000)
            }
        }
    }
    fun cancelCountdown() {
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