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
    val loggedUserId: Int
) {

    fun layoutGravity() = if (messages.senderUser.toInt() == loggedUserId) Gravity.END else Gravity.START

    fun textBubbleVisibility() = if (messages.messageType == "text") View.VISIBLE else View.GONE
    fun imageBubbleVisibility() = if (messages.messageType == "image") View.VISIBLE else View.GONE

    fun messageBackground(context: Context) : Drawable? = if(messages.senderUser.toInt() == loggedUserId) {
        ContextCompat.getDrawable(context, R.drawable.message_outgoing)
    }else{
        ContextCompat.getDrawable(context, R.drawable.message_incoming)
    }

    fun getText() = if (messages.messageType == "text") messages.message else "null"
    fun getImage() = if (messages.messageType == "image") messages.message else "null"

    fun textColor(context: Context) : Int = if (messages.senderUser.toInt() == loggedUserId) ContextCompat.getColor(context, R.color.white) else ContextCompat.getColor(context, R.color.black)

    fun getMsgTime() = messages.messageTime
}