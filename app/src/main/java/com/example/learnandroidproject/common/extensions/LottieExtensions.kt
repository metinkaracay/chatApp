package com.example.learnandroidproject.common.extensions

import android.animation.Animator
import com.airbnb.lottie.LottieAnimationView

fun LottieAnimationView.onAnimationStart(animationStart: () -> Unit) {
    this.removeAllAnimatorListeners()
    this.addAnimatorListener(object : Animator.AnimatorListener {

        override fun onAnimationStart(animation: Animator) {
            animationStart.invoke()
        }

        override fun onAnimationEnd(animation: Animator) {}

        override fun onAnimationCancel(animation: Animator) {}

        override fun onAnimationRepeat(animation: Animator) {}
    })
}

fun LottieAnimationView.onAnimationEnd(animationEnd: () -> Unit) {
    this.removeAllAnimatorListeners()
    this.addAnimatorListener(object : Animator.AnimatorListener {

        override fun onAnimationStart(animation: Animator) {}

        override fun onAnimationEnd(animation: Animator) {
            animationEnd.invoke()
        }

        override fun onAnimationCancel(animation: Animator) {}

        override fun onAnimationRepeat(animation: Animator) {}
    })
}

fun LottieAnimationView.onAnimationRepeat(animationRepeat: () -> Unit) {
    this.removeAllAnimatorListeners()
    this.addAnimatorListener(object : Animator.AnimatorListener {

        override fun onAnimationStart(animation: Animator) {}

        override fun onAnimationEnd(animation: Animator) {}

        override fun onAnimationCancel(animation: Animator) {}

        override fun onAnimationRepeat(animation: Animator) {
            animationRepeat.invoke()
        }
    })
}