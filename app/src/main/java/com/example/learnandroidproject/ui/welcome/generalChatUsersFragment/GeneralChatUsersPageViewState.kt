package com.example.learnandroidproject.ui.welcome.generalChatUsersFragment

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo

class GeneralChatUsersPageViewState(
    val users: List<UserInfo>
) {

    fun toolbarColor(context: Context): Int = ContextCompat.getColor(context, R.color.toolbar_color)
    fun backArrow(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.back_arrow_icon)
    fun getHeader() = "Chat App"
}