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

    var messageList: List<MessageItem> = arrayListOf()
    var group: GroupInfo = GroupInfo(0,"","","null","null",false) // TODO seeni düzeltmen gerekebilir
    private var pageId = 1
    var isNewChat = true
    var fetchSocketData = false // mesajları biriktirdiği için chat'e ilk girdiğinde son mesajı birkaç kez yazdırıyordu. Onu düzeltmek için kontrol
    var sendingMessage: MutableMap<String, MutableList<Args>> = mutableMapOf()
    var members: List<GroupMember> = arrayListOf()
    var loggedUserid = 0
    var raceStatus = 0

    var isAdmin = false
    var isRaceStart = false
    var timerPopUpVisibility = false
    private var currentAnimation: ValueAnimator? = null //Fragmentta kontrol et

    // Yarışta sıralama hesaplamak ve animasyonu oynatabilmek için gerken değerler
    //var userImageViews: List<ShapeableImageView> = arrayListOf()
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
                    if (isAdmin == false && raceDatas.isNotEmpty()){
                        setRaceState(true)
                        startCountdown(raceRemainingTime.toString())
                        Log.e("raceDatas","$raceDatas")
                    }

                    _groupChattingPageViewStateLiveData.value = GroupChattingPageViewState(group,messages,isAdmin,isRaceStart)
                    messageList = messages + messageList
                    pageId++
                    fetchSocketData = true
                    _messageFetchRequestLiveData.postValue(true)
                }
            }
        }
    }

    fun sendMessagesToPageViewState(list: List<MessageItem>){
        _groupChattingPageViewStateLiveData.value = _groupChattingPageViewStateLiveData.value?.copy(messages = list)
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

    fun finishEvent(statusCode: Int): Boolean{
        // Adminse ve etkinlik bitmeden odadan çıkmaya çalışıyorsa etkinliği erken bitirir
        if (isAdmin){
            raceStatus = statusCode
            cancelCountdown()
        }else{
            return false
        }
        return true
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

            Log.e("gelen ack","$ackReceiver")
            if (ackReceiver == 0 && members.size > 3){
                setRaceState(true)
                startCountdown(remainingTime)
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
            _groupChattingPageViewStateLiveData.value = _groupChattingPageViewStateLiveData.value?.copy(isRaceStart = isRaceStart)
        }
        setupUsers()
    }

    fun setTimerPopUp(state: Boolean){
        Log.e("gelen race state","$state")
        _groupChattingPageViewStateLiveData.value = _groupChattingPageViewStateLiveData.value?.copy(popUpVisibility = state)
    }

    fun setupUsers() {
        userPoints.clear()
        members.filter { it.uRole == "User" }.forEach { user ->
            userPoints[user.uId] = 0
        }
        Log.e("race_users","$userPoints")
    }

    fun updateUserPoints(args: RaceData) {
        Log.e("race_args","$args")
        val userId = args.userId
        val point = args.point ?: 0

        if (userPoints.containsKey(userId.toInt())) {
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
        userPositionPercentages = mutableListOf<Float>()
        var userNamesList = mutableListOf<String>()

        for (i in 0 until topThreeUserIds.size){
            Log.e("race_total_öncesi","gelen : ${userPoints[topThreeUserIds[i]]}")
            totalPoint += userPoints[topThreeUserIds[i]] ?: 0
        }

        for (userId in topThreeUserIds) {
            val user = members.find { it.uId == userId }
            val photoUrl = user?.uPhoto ?: "null"
            userNamesList.add(photoUrl)
        }

        for (userId in topThreeUserIds) {
            val positionPercent = userPoints[userId]!!.toFloat() / totalPoint
            userPositionPercentages.add(positionPercent)
            Log.e("race_user_percent","user: $userId,percent = $userPositionPercentages")
        }

        _positionPercentsCalculatedLiveData.postValue(true)

        for (j in 0 until userImageViews.size) {

            val cardView = userImageViews[j]
            val initialX = cardView.x
            Log.e("frameLAyout","${frameLayoutWidth}")
            Log.e("frameLayout1","${cardView.width}")
            Log.e("frameLayout2","${userPositionPercentages[j]}")
            val targetX = (frameLayoutWidth - cardView.width) * userPositionPercentages[j]
            animateUserPosition(cardView, initialX, targetX)
        }
        _groupChattingPageViewStateLiveData.value = _groupChattingPageViewStateLiveData.value?.copy(users = userNamesList.toList())
    }

    private fun animateUserPosition(userImageView: CardView, initialX: Float, targetX: Float) {
        Log.e("gelen imageView","$userImageView")
        Log.e("gelen initialx","$initialX")
        Log.e("gelen targetX","$targetX")
        val animator = ValueAnimator.ofFloat(initialX, targetX)
        animator.duration = 100
        animator.interpolator = AccelerateDecelerateInterpolator()

        animator.addUpdateListener { valueAnimator ->
            val animatedValue = valueAnimator.animatedValue as Float
            userImageView.x = animatedValue
        }

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                currentAnimation = null
            }
        })

        currentAnimation = animator
        animator.start()
    }

    fun startCountdown(remainingTime: String) {
        countdownJob?.cancel()

        countdownJob = viewModelScope.launch(Dispatchers.IO) {
            for (i in remainingTime.toInt() downTo 0) {
                withContext(Dispatchers.Main) {
                    _groupChattingPageViewStateLiveData.value = _groupChattingPageViewStateLiveData.value?.copy(remainingTime = i.toString())
                }
                if (i == 0 ){
                    cancelCountdown()
                }
                delay(1000)
            }
        }
    }
    fun cancelCountdown() {
        Log.e("countDowner","canceled")
        countdownJob?.cancel()
        viewModelScope.launch(Dispatchers.Main){
            _groupChattingPageViewStateLiveData.value = _groupChattingPageViewStateLiveData.value?.copy(isRaceStart = false)
        }
    }

    fun checkRemainingTime(remainingTime: String): Boolean{
        if (remainingTime.isNullOrEmpty()){
            _errorMessageLiveData.postValue("Süre Belirlemediniz")
            return false
        }else if(remainingTime.toInt() > 301){
            _errorMessageLiveData.postValue("Etkinlik 5 dakikadan fazla süremez")
            return false
        }
        return true
    }
}