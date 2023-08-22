package com.example.learnandroidproject.ui.welcome.baseChatRooms

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R

class BaseChatRoomsPageViewState(
    val url: String
) {
    fun toolbarColor(context: Context): Int = ContextCompat.getColor(context,R.color.toolbar_color)
    fun selectedPhotoVisibility() = View.VISIBLE
    fun selectedProfilePhoto() = url
    fun getToolbarHeader() = "Chat App"
    fun exitButton(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.exit)
    fun allUsersButtonBackground(context: Context): Int = ContextCompat.getColor(context, R.color.send_message_color)
    fun allUsersLogo(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.users_logo)
}