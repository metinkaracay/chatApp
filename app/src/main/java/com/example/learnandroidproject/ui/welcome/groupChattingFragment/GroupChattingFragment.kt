package com.example.learnandroidproject.ui.welcome.groupChattingFragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.core.view.doOnLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.alphamovie.lib.AlphaMovieView
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupInfo
import com.example.learnandroidproject.databinding.FragmentGroupChattingBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.example.learnandroidproject.ui.welcome.WelcomeViewModel
import com.example.learnandroidproject.ui.welcome.chattingFragment.SocketHandler
import com.example.learnandroidproject.ui.welcome.groupChattingFragment.adapter.GroupChattingMessagesAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GroupChattingFragment : BaseFragment<FragmentGroupChattingBinding>() {

    @Inject
    lateinit var recyclerAdapter: GroupChattingMessagesAdapter
    private val viewModel: GroupChattingViewModel by viewModels()
    private val welcomeViewModel: WelcomeViewModel by activityViewModels()

    private var currentAnimation: ValueAnimator? = null

    private var isFirstData = true
    override fun getLayoutResId(): Int = R.layout.fragment_group_chatting

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireContext().getSharedPreferences("LoggedUserID",Context.MODE_PRIVATE)
        val loggedUserId = sharedPreferences.getString("LoggedUserId","")
        welcomeViewModel.groupMessageSingleLiveEvent.observe(viewLifecycleOwner){
            viewModel.fetchMessagesOnSocket(it)
        }
        val group = welcomeViewModel.getGroupInfo()
        viewModel.group = group // Tıklanan grup bilgilerini çeker
        recyclerAdapter = GroupChattingMessagesAdapter()
        initResultsItemsRecyclerView()
        handleViewOptions()
        val senderUserMessage = welcomeViewModel.getLastSentMessage()
        viewModel.sendingMessage = senderUserMessage!!
        with(viewModel){
            groupChattingPageViewStateLiveData.observeNonNull(viewLifecycleOwner){
                with(binding){
                    pageViewState = it
                    executePendingBindings()
                }
                recyclerAdapter.setItems(it.messages,loggedUserId!!.toInt(),it.membersNameList)
            }
            newMessageOnTheChatLiveData.observeNonNull(viewLifecycleOwner){
                if (it){
                    binding.recyclerView.scrollToPosition(this.messageList.size - 1)
                }
            }
            errorMessageLiveData.observe(viewLifecycleOwner){
                Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
            }
            eventStateLiveData.observeNonNull(viewLifecycleOwner){
                welcomeViewModel.adminStartedEvent(it)
            }
            positionPercentsCalculatedLiveData.observeNonNull(viewLifecycleOwner){
                if (it){
                    calculateLocation()
                    ghostCarController(loggedUserId!!.toInt())
                }
            }
        }
        with(welcomeViewModel){
            raceDataLiveEvent.observeNonNull(viewLifecycleOwner){
                // Socketten gelen yarışma verileri
                if (viewModel.group.groupId == it[0].groupId && !isFirstData){
                    viewModel.updateUserPoints(it)
                }else{
                    isFirstData = false // Grupta değilken mesajları biriktiriyor ve girdiğimde hem get'ten çekiyor hemde sockettekini işliyor. Verilerin hatalı olmasına sebep oluyordu
                }
            }
        }
        viewModel.fetchMessages(requireContext())

        binding.user1.doOnLayout {// Süre bittiğinde araçları çekmeyi sağlayan kodu besliyor
            viewModel.userImageViews = listOf(binding.user1, binding.user2, binding.user3,binding.user4)
        }
        setCardStartLocation()
        //roadVideo()
        playLotties()
        ghostCarController(loggedUserId!!.toInt())
    }

    fun handleViewOptions(){
        binding.backArrow.setOnClickListener {
            welcomeViewModel.exitToChatRoomFillData(true)
            welcomeViewModel.navigateUp()
        }
        binding.sendButton.setOnClickListener {
            val message = binding.editText.text.toString()
            viewModel.sendMessage(SocketHandler,requireContext(),message)
            binding.editText.text.clear()

            welcomeViewModel.fillTestSingleEvent(message)
        }
        binding.groupInfo.setOnClickListener {
            welcomeViewModel.goToChatInfoFragment()
        }
        viewModel.messageFetchRequestLiveData.observe(viewLifecycleOwner){
            binding.swipeRefreshLayout.setOnRefreshListener {
                viewModel.fetchMessages(requireContext())
                binding.swipeRefreshLayout.isRefreshing = false
            }
            if (!viewModel.isNewChat){
                binding.swipeRefreshLayout.isEnabled = false
                Toast.makeText(requireContext(),"Tüm Mesajlar Yüklendi", Toast.LENGTH_SHORT).show()
            }
            // Grup Ayrıntılarına tıklandığında gruptaki kişileri görebilmesi için verileri yollar
            welcomeViewModel.membersList = viewModel.members
        }
        binding.editText.setOnClickListener {
            binding.recyclerView.scrollToPosition(viewModel.messageList.size - 1 )
        }
        binding.buttonRace.setOnClickListener {
            viewModel.setTimerPopUp(true)
        }
        binding.startButton.setOnClickListener {
            val remainingTime = binding.timeEditText.text.toString()
            val result = viewModel.checkRemainingTime(remainingTime)

            if (result){
                // Yarış başlatırken elimizde olması gereken parametreler

                viewModel.raceStatus = 0
                viewModel.startToRace(SocketHandler,remainingTime)
                viewModel.setTimerPopUp(false)

                // Klavyeyi kapatma işlemi
                val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
            }
        }
    }

    fun ghostCarController(id: Int){
        val topThree = viewModel.userRaceDatas

        val existingUser = topThree.find { it.userId == id }

        if (existingUser != null){
            Log.e("GhostController","ilk 3'te hocam")
            viewModel.ghostCarVisibility(false, id)
        }else{
            viewModel.ghostCarVisibility(true, id)
            Log.e("GhostController","ilk 3'te değil hocam")
            //playCarVideo(4)
        }
    }

    fun calculateLocation(){
        val cards = listOf(binding.user1, binding.user2, binding.user3,binding.user4)
        val cars = mutableListOf(0,1,2,3)
        val currentCars = mutableListOf<Int>()
        val topThree = viewModel.userRaceDatas

        if (!viewModel.userRaceDatas.isNullOrEmpty()) {
            for (i in 0 until topThree.size) {
                Log.e("userPercent", "${viewModel.userRaceDatas[i].racePercent}")
                val cardId = topThree[i].carId
                val cardView = cards[cardId]
                val initialX = cardView.x
                val targetX = (binding.innerFrameLayout.width - cardView.width) * viewModel.userRaceDatas[i].racePercent
                if (targetX != 0.0f) {
                    animateUserPosition(cardView, initialX, targetX)
                }
                currentCars.add(topThree[i].carId)

                //playCarVideo(cardId)
            }
            // Yarışa 4. girdiği zaman önceki ilk 3'ten düşeni çıkarır
            for (i in 0 until cars.size){
                if (!currentCars.contains(cars[i])){
                    val card = cards[cars[i]]
                    animateUserPosition(card, card.x, -250f)
                }
            }
            Log.e("userPercent","-----------------------------------")
        }
    }

    /*private fun playCarVideo(carId: Int) {

        val carIdToVideoMapping = mapOf(
            0 to "green_car",
            1 to "red_car",
            2 to "yellow_car",
            3 to "black_car",
            4 to "yellow_car"
        )

        val videoFile = carIdToVideoMapping[carId]
        if (videoFile != null) {
            val videoResourceID = resources.getIdentifier(videoFile, "raw", requireContext().packageName)
            val videoUri = Uri.parse("android.resource://${requireContext().packageName}/$videoResourceID")
            Log.e("kontrollll","$videoUri, file : $videoFile")
            when (carId) {
                0 -> playVideo(binding.video, videoUri)
                1 -> playVideo(binding.video2, videoUri)
                2 -> playVideo(binding.video3, videoUri)
                3 -> playVideo(binding.video4, videoUri)
                4 -> playVideo(binding.video5, videoUri)
            }
        }
    }*/

    private fun animateUserPosition(userImageView: CardView, initialX: Float, targetX: Float) {
        val animator = ValueAnimator.ofFloat(initialX, targetX)
        animator.duration = 300
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

    private fun playVideo(videoView: AlphaMovieView, videoUri: Uri) {
        try {
            videoView.setVideoFromUri(requireContext(), videoUri)
            videoView.setLooping(true)
        } catch (e: Exception) {
            Log.e("RaceFragment", "playVideo: ${e.localizedMessage}")
        }
    }

    fun setCardStartLocation(){
        val cards = listOf(binding.user1, binding.user2, binding.user3, binding.user4) // Geçici Çözüm
        for (i in 0 until cards.size) {
            val card = cards[i]
            card.x = -250f
        }
    }
    fun playLotties(){
        binding.road.setImageAssetsFolder("images/")
        binding.car1.setImageAssetsFolder("images/")
        binding.car2.setImageAssetsFolder("images/")
        binding.car3.setImageAssetsFolder("images/")
        binding.car4.setImageAssetsFolder("images/")
        binding.car5.setImageAssetsFolder("images/")
    }

    /*fun roadVideo(){
        val videoResourceID = resources.getIdentifier("road", "raw", requireContext().packageName)
        val videoUri = Uri.parse("android.resource://${requireContext().packageName}/$videoResourceID")

        binding.road.setVideoURI(videoUri)
        binding.road.requestFocus()
        binding.road.start()
        binding.road.setOnPreparedListener { it.isLooping = true }
    }*/

    private fun initResultsItemsRecyclerView() {
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.VERTICAL
                stackFromEnd= true
            }
            adapter = recyclerAdapter
        }
    }
}