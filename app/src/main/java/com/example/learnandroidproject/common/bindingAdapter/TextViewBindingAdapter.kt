package com.example.learnandroidproject.common.bindingAdapter

import android.graphics.Typeface
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.learnandroidproject.common.extensions.toHtml

object TextViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("htmlText")
    fun htmlText(textView: TextView, text: String?) {
        textView.text = text.orEmpty().toHtml()
    }

    @JvmStatic
    @BindingAdapter("customFont")
    fun setTypeface(textView: TextView, font: Typeface?) {
        font?.let { textView.typeface = it }
    }
}