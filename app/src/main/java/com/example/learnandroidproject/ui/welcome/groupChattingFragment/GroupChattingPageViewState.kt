package com.example.learnandroidproject.ui.welcome.groupChattingFragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem

class GroupChattingPageViewState(
    val group: GroupInfo,
    val messages: List<MessageItem>
) {
    fun backArrow(context: Context) : Drawable? = ContextCompat.getDrawable(context, R.drawable.back_arrow_icon)

    fun defaultPhotoVisibility() = if (group.groupPhoto == "null") View.VISIBLE else View.GONE

    fun defaultPhoto(context: Context) : Drawable? = ContextCompat.getDrawable(context, R.drawable.avatar)

    fun selectedPhotoVisibility() = if (group.groupPhoto != "null") View.VISIBLE else View.GONE

    fun selectedPhoto() = group.groupPhoto

    fun getUserName() = group.groupName

    fun editTextHint() = "Mesaj"

    fun sendButtonBackground(context: Context): Int = ContextCompat.getColor(context, R.color.send_message_color)

    fun sendIcon(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.send_icon)
}