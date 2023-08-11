package com.example.learnandroidproject.ui.welcome.groupChattingFragment.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem

class GroupMessageItemPageViewState(
    val message: MessageItem,
    val loggedUserId: Int
) {

    fun layoutGravity() = if (message.senderUser.toInt() == loggedUserId) Gravity.END else Gravity.START

    fun getMessageSenderName() = message.senderUser

    fun userNameTextColor(context: Context): Int = ContextCompat.getColor(context, R.color.black)

    fun senderNameGravity() = if (message.senderUser.toInt() == loggedUserId) Gravity.END else Gravity.START

    fun textBubbleVisibility() = View.VISIBLE

    fun messageBackground(context: Context) : Drawable? = if(message.senderUser.toInt() == loggedUserId) {
        ContextCompat.getDrawable(context, R.drawable.message_outgoing)
    }else{
        ContextCompat.getDrawable(context, R.drawable.message_incoming)
    }

    fun getText() = message.message

    fun textColor(context: Context) : Int = if (message.senderUser.toInt() == loggedUserId) ContextCompat.getColor(context, R.color.white) else ContextCompat.getColor(context, R.color.black)

    fun getMsgTime() = message.messageTime
}