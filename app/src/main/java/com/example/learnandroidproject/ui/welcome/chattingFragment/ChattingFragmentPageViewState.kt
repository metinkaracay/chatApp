package com.example.learnandroidproject.ui.welcome.chattingFragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem

data class ChattingFragmentPageViewState(
    val userInfo: UserInfo,
    val messages: List<MessageItem>,
    var isLoaded: Boolean = false
    )
{
    fun loadingScreenVisibility() = if (!isLoaded) View.VISIBLE else View.GONE
    fun backArrow(context: Context) : Drawable? = ContextCompat.getDrawable(context, R.drawable.back_arrow_icon)
    fun selectedPhoto() = userInfo.uPhoto
    fun getUserName() = userInfo.uName
    fun galleryIcon(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.gallery_icon)
    fun editTextHint() = "Mesaj"
    fun sendButtonBackground(context: Context): Int = ContextCompat.getColor(context,R.color.send_message_color)
    fun sendIcon(context: Context): Drawable? = ContextCompat.getDrawable(context,R.drawable.send_icon)
}