package com.example.learnandroidproject.common.bindingAdapter

import androidx.databinding.BindingAdapter
import com.google.android.material.card.MaterialCardView

object CardViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("strokeWidth")
    fun setLayoutWidth(cardView: MaterialCardView, strokeWidth: Int) {
        cardView.strokeWidth = strokeWidth
    }
}