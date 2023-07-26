package com.example.learnandroidproject.ui.welcome.chattingFragment.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R

class MessageItemPageViewState {

    fun layoutGravity() = Gravity.END

    fun textBubble() = View.VISIBLE

    fun messageBackground(context: Context) : Drawable? = ContextCompat.getDrawable(context, R.drawable.message_outgoing)

    fun getText() = "test" //TODO yazılan mesaj

    fun textColor(context: Context) : Int = ContextCompat.getColor(context, R.color.white)
}