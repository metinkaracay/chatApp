package com.example.learnandroidproject.ui.welcome.createGroupFragment.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo

data class CreateGroupUserItemPageViewState(
    private val user: UserInfo,
    val selectedUsers: List<Int>
) {

    fun getUserName() = user.uName

    fun defaultPhotoVisibility() = if (user.uPhoto == "null") View.VISIBLE else View.GONE

    fun defaultPhoto(context: Context): Drawable? = if (selectedUsers.contains(user.uId)) ContextCompat.getDrawable(context, R.drawable.check_icon) else ContextCompat.getDrawable(context, R.drawable.avatar)

    fun selectedPhotoVisibility() = if (user.uPhoto != "null" && !selectedUsers.contains(user.uId)) View.VISIBLE else View.GONE

    fun checkMark(context: Context) : Drawable? = ContextCompat.getDrawable(context, R.drawable.check_icon)
    fun checkMarkVisibility() = if (selectedUsers.contains(user.uId)) View.VISIBLE else View.GONE

    fun checkBackgroundColor(context: Context): Int = ContextCompat.getColor(context, R.color.online_color)

    fun selectedPhoto() = user.uPhoto

    fun getStatus() = user.uStatu
}