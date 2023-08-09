package com.example.learnandroidproject.ui.welcome.chattingFragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem

data class ChattingFragmentPageViewState(
    val userInfo: UserInfo,
    val messages: List<MessageItem>
    )
{
    fun backArrow(context: Context) : Drawable? = ContextCompat.getDrawable(context, R.drawable.back_arrow_icon)
    fun defaultPhotoVisibility() = if (userInfo.uPhoto == "null") View.VISIBLE else View.GONE
    fun defaultPhoto(context:Context) : Drawable? = ContextCompat.getDrawable(context, R.drawable.avatar)
    fun selectedPhotoVisibility() = if (userInfo.uPhoto != "null") View.VISIBLE else View.GONE
    fun selectedPhoto() = userInfo.uPhoto
    fun getUserName() = userInfo.uName
    fun editTextHint() = "Mesaj"
    fun sendButtonBackground(context: Context): Int = ContextCompat.getColor(context,R.color.send_message_color)
    fun sendIcon(context: Context): Drawable? = ContextCompat.getDrawable(context,R.drawable.send_icon)
}