package com.example.learnandroidproject.ui.welcome.groupChattingFragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.doOnLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.Recycler
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
                recyclerAdapter.setItems(it.messages,loggedUserId!!.toInt())
            }
            newMessageOnTheChatLiveData.observeNonNull(viewLifecycleOwner){
                if (it){
                    binding.recyclerView.scrollToPosition(this.messageList.size - 1)
                }
            }
            errorMessageLiveData.observe(viewLifecycleOwner){
                Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
            }
            /*positionPercentsCalculatedLiveData.observeNonNull(viewLifecycleOwner){
                if (it){
                    Log.e("calculate","çalıştı")
                    calculateLocation()
                }
            }*/
        }
        with(welcomeViewModel){
            raceDataLiveEvent.observeNonNull(viewLifecycleOwner){
                // Socketten gelen yarışma verileri
                viewModel.updateUserPoints(it)
            }
        }
        viewModel.fetchMessages(requireContext())

        binding.user1.doOnLayout {
            viewModel.userImageViews = listOf(binding.user1, binding.user2, binding.user3)
            viewModel.frameLayoutWidth = binding.innerFrameLayout.width
        }
    }

    fun handleViewOptions(){
        binding.backArrow.setOnClickListener {
            // Admin, etkinlik bitmeden çıkmak isterse çalışır
            val result = viewModel.finishEvent(1)
            if (result){
                viewModel.startToRace(SocketHandler,"0")
            }

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
            if (it){
                binding.swipeRefreshLayout.setOnRefreshListener {
                    viewModel.fetchMessages(requireContext())
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }else{
                if (!viewModel.isNewChat){
                    binding.swipeRefreshLayout.isEnabled = false
                    Toast.makeText(requireContext(),"Tüm Mesajlar Yüklendi", Toast.LENGTH_SHORT).show()
                }
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

    fun calculateLocation(){
        val userImageViews = listOf(binding.user1, binding.user2, binding.user3)

        for (j in 0 until userImageViews.size) {

            val cardView = userImageViews[j]
            val initialX = cardView.x
            val targetX = (binding.innerFrameLayout.width - cardView.width) * viewModel.userPositionPercentages[j]
            Log.e("frameLAyout","${binding.innerFrameLayout.width}")
            Log.e("frameLayout1","${cardView.width}")
            animateUserPosition(cardView, initialX, targetX)
        }
    }

    fun animateUserPosition(userImageView: CardView, initialX: Float, targetX: Float) {
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