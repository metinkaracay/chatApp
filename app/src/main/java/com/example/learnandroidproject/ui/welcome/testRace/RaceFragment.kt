package com.example.learnandroidproject.ui.welcome.testRace

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import com.example.learnandroidproject.R
import com.example.learnandroidproject.databinding.FragmentRaceBinding
import com.example.learnandroidproject.ui.base.BaseFragment
import com.google.android.material.imageview.ShapeableImageView

class RaceFragment : BaseFragment<FragmentRaceBinding>() {

    private lateinit var userImageViews: List<ShapeableImageView>
    private lateinit var userButtons: List<Button>

    private var userPoints: MutableList<Int> = mutableListOf(0, 0, 0)
    private var currentAnimation: ValueAnimator? = null

    override fun getLayoutResId(): Int = R.layout.fragment_race

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userImageViews = listOf(binding.user, binding.user2, binding.user3)

        userButtons = listOf(binding.user1button, binding.user2button, binding.user3button)
        setupButtonClickListeners()
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
        /*if (selectedUserIndex == 0){
            userPoints[selectedUserIndex] = userPoints[selectedUserIndex] + 1
        }else if (selectedUserIndex == 1){
            userPoints[selectedUserIndex] = userPoints[selectedUserIndex] + 5
        }else if (selectedUserIndex == 2){
            userPoints[selectedUserIndex] = userPoints[selectedUserIndex] + 10
        }*/
        userPoints.forEachIndexed { index, points ->
            userButtons[index].text = points.toString()
        }
    }

    private fun updateUsersPosition() {
        val totalPoints = userPoints.sum().toFloat()
        Log.e("race_log_1", "$totalPoints")

        val positionPercent = userPoints.map { it.toFloat() / totalPoints }
        Log.e("race_log_2","$positionPercent")
        for (i in 0 until userImageViews.size) {
            val userImageView = userImageViews[i]
            val initialX = userImageView.x
            val targetX = (binding.innerFrameLayout.width - userImageView.width) * positionPercent[i]
            Log.e("userImageView","$userImageView")
            animateUserPosition(userImageView, initialX, targetX)
        }
    }

    private fun animateUserPosition(userImageView: ShapeableImageView, initialX: Float, targetX: Float) {
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
}