package com.example.learnandroidproject.ui.welcome.chattingFragment.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem

data class MessageItemPageViewState(
    val messages: MessageItem,
    val uId: Int?
) {

    fun layoutGravity() = if (messages.senderUser.toInt() == uId) Gravity.START else Gravity.END

    fun textBubbleVisibility() = View.VISIBLE

    fun messageBackground(context: Context) : Drawable? = if(messages.senderUser.toInt() == uId) {
        ContextCompat.getDrawable(context, R.drawable.message_incoming)
    }else{
        ContextCompat.getDrawable(context, R.drawable.message_outgoing)
    }

    fun getText() = messages.message

    fun textColor(context: Context) : Int = if (messages.senderUser.toInt() == uId) ContextCompat.getColor(context, R.color.black) else ContextCompat.getColor(context, R.color.white)

    fun getMsgTime() = messages.messageTime
}