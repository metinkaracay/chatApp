package com.example.learnandroidproject.ui.welcome.friendsChatUsersFragment.adapter

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.fonts.Font
import android.graphics.fonts.FontFamily
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo

class FriendsUsersItemPageViewState(
    private val users: UserInfo,
    val mElapsedTime: String
) {

    fun getUserName() = users.uName

    fun defaultPhotoVisibility() = if (users.uPhoto == "null") View.VISIBLE else View.GONE

    fun defaultPhoto(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.avatar)

    fun selectedPhotoVisibility() = if (users.uPhoto != "null") View.VISIBLE else View.GONE

    fun selectedPhoto() = users.uPhoto

    fun getLastMessage() = users.lastMessage

    fun lastMessageFont(context: Context): Typeface? = if (users.seen == false) ResourcesCompat.getFont(context,R.font.montserrat_bold) else ResourcesCompat.getFont(context,R.font.montserrat_regular)

    fun getElapsedTime() = mElapsedTime
}