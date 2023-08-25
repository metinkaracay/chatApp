package com.example.learnandroidproject.ui.welcome.testRace

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.cardview.widget.CardView
import com.alphamovie.lib.AlphaMovieView
import com.example.learnandroidproject.R
import com.example.learnandroidproject.databinding.FragmentRaceBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import java.io.File

class RaceFragment : BaseFragment<FragmentRaceBinding>() {

    private lateinit var userImageViews: List<ShapeableImageView>
    private lateinit var carVideos: List<AlphaMovieView>
    private lateinit var cards: List<MaterialCardView>
    private lateinit var userButtons: List<Button>
    private var videoIndex = 0
    private val videoFiles = listOf("yellow_car", "red_car", "green_car")

    private var userPoints: MutableList<Int> = mutableListOf(0, 0, 0)
    private var currentAnimation: ValueAnimator? = null

    override fun getLayoutResId(): Int = R.layout.fragment_race

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val files = listOf("road","yellow_car","red_car","green_car")

        /*for (i in files) {
            val videoResourceID = resources.getIdentifier(i, "raw", requireContext().packageName)
            val videoUri = Uri.parse("android.resource://${requireContext().packageName}/$videoResourceID")

            if (i == "road") {
                binding.road.setVideoURI(videoUri)
                binding.road.requestFocus()
                binding.road.start()
                binding.road.setOnPreparedListener { it.isLooping = true }
            } else {
                when (i) {
                    "green_car" -> playVideo(binding.video, videoUri)
                    "red_car" -> playVideo(binding.video2, videoUri)
                    "yellow_car" -> playVideo(binding.video3, videoUri)
                }
            }
        }*/

        //userImageViews = listOf(binding.user, binding.user2, binding.user3)
        cards = listOf(binding.user1, binding.user2, binding.user3)

        userButtons = listOf(binding.user1button, binding.user2button, binding.user3button)
        setupButtonClickListeners()

        val videoResourceID = resources.getIdentifier("road", "raw", requireContext().packageName)
        val videoUri = Uri.parse("android.resource://${requireContext().packageName}/$videoResourceID")

        binding.road.setVideoURI(videoUri)
        binding.road.requestFocus()
        binding.road.start()
        binding.road.setOnPreparedListener { it.isLooping = true }

        // Start creating videos with a delay
        createVideoWithDelay()
        setCardStartLocation()
    }

    fun setCardStartLocation(){
        for (i in 0 until cards.size) {
            val card = cards[i]
            card.x = -190f
        }
    }

    private fun createVideoWithDelay() {
        if (videoIndex < videoFiles.size) {
            val videoFile = videoFiles[videoIndex]
            val videoResourceID = resources.getIdentifier(videoFile, "raw", requireContext().packageName)
            val videoUri = Uri.parse("android.resource://${requireContext().packageName}/$videoResourceID")

            when (videoFile) {
                "yellow_car" -> playVideo(binding.video, videoUri)
                "red_car" -> playVideo(binding.video2, videoUri)
                "green_car" -> playVideo(binding.video3, videoUri)
            }
            videoIndex++

            Handler().postDelayed({
                createVideoWithDelay()
            }, 500)
        }
    }

    private fun playVideo(videoView: AlphaMovieView,videoUri: Uri) {
        try {
            videoView.setVideoFromUri(requireContext(), videoUri)
            videoView.setLooping(true)
        } catch (e: Exception) {
            Log.e("RaceFragment", "playVideo: ${e.localizedMessage}")
        }
    }

    private fun setupButtonClickListeners() {
        userButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                updateUserPoints(index)
                updateUsersPosition() // Kullanıcı puanları güncellendikten sonra konumları güncelle
            }
        }
    }

    private fun updateUserPoints(selectedUserIndex: Int) {
        userPoints[selectedUserIndex]++
        userPoints.forEachIndexed { index, points ->
            userButtons[index].text = points.toString()
        }
    }

    private fun updateUsersPosition() {
        val totalPoints = userPoints.sum().toFloat()
        Log.e("race_log_1", "$totalPoints")

        val positionPercent = userPoints.map { it.toFloat() / totalPoints }
        Log.e("race_log_2","$positionPercent")
        for (i in 0 until cards.size) {
            val card = cards[i]
            val initialX = card.x
            val targetX = (binding.innerFrameLayout.width - card.width) * positionPercent[i]
            if (targetX !=0.0f){
                animateUserPosition(card, initialX, targetX)
            }
        }
    }

    private fun animateUserPosition(card: MaterialCardView, initialX: Float, targetX: Float) {
        val animator = ValueAnimator.ofFloat(initialX, targetX)
        animator.duration = 1000
        animator.interpolator = AccelerateDecelerateInterpolator()

        animator.addUpdateListener { valueAnimator ->
            val animatedValue = valueAnimator.animatedValue as Float
            card.x = animatedValue
        }

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                currentAnimation = null
            }
        })

        currentAnimation = animator
        animator.start()
    }
}