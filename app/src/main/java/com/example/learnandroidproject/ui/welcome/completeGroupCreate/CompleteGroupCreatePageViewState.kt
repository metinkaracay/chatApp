package com.example.learnandroidproject.ui.welcome.completeGroupCreate

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R

class CompleteGroupCreatePageViewState {

    fun toolbarColor(context: Context): Int = ContextCompat.getColor(context, R.color.toolbar_color)

    fun backArrow(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.back_arrow_icon)

    fun getHeader() = "Chat App"

    fun getHint() = "Grup Adı Giriniz"

    fun buttonBackgroundColor(context: Context): Int = ContextCompat.getColor(context, R.color.online_color)

    fun completeButton(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.send_icon)
}