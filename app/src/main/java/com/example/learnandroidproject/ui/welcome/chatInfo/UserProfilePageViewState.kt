package com.example.learnandroidproject.ui.welcome.chatInfo

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.request.userRequest.User

class UserProfilePageViewState(
    val user: User
) {

    fun toolbarColor(context: Context): Int = ContextCompat.getColor(context, R.color.toolbar_color)

    fun backArrow(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.back_arrow_icon)

    fun getHeader() = "Chat App"

    fun defaultPhotoVisibility() = if (user.photo == "null") View.VISIBLE else View.GONE

    fun userDefaultPhoto(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.avatar)

    fun selectedPhotoVisibility() = if (user.photo != "null") View.VISIBLE else View.GONE

    fun selectedPhoto() = user.photo

    fun getUserName() = user.firstName+" "+user.lastName

    fun getAgeHeader() = "Yaş: "

    fun getUserAge() = user.age

    fun getStatusHeader() = "Durumu: "

    fun getUserStatus() = user.status
}