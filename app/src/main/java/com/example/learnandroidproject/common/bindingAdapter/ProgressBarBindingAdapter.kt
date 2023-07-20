package com.example.learnandroidproject.common.bindingAdapter

import android.animation.ObjectAnimator
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter

object ProgressBarBindingAdapter {

    @JvmStatic
    @BindingAdapter("animatedProgress")
    fun setCustomProgressBar(progressBar: ProgressBar, progress: Int) {
        ObjectAnimator.ofInt(progressBar, "progress", progressBar.progress, progress).apply {
            duration = 10000
            interpolator = DecelerateInterpolator()
        }.start()
    }
}