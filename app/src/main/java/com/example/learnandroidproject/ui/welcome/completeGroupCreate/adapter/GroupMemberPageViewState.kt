package com.example.learnandroidproject.ui.welcome.completeGroupCreate.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo

data class GroupMemberPageViewState(
    val member: UserInfo
) {
    fun defaultPhotoVisibility() = if (member.uPhoto == "null") View.VISIBLE else View.GONE

    fun userPhotoDefault(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.avatar)

    fun selectedPhotoVisibility() = if (member.uPhoto != "null") View.VISIBLE else View.GONE

    fun userPhoto() = member.uPhoto

    fun getUserName() = member.uName

    fun cancelIcon(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.cross_icon)
}