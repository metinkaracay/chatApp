package com.example.learnandroidproject.common.bindingAdapter

import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView

object LottieViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("animName")
    fun setLayoutWidth(lottieView: LottieAnimationView, animationName: String) {
        lottieView.setAnimation(animationName)
    }
}