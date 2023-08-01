package com.example.learnandroidproject.ui.welcome.generalChatUsersFragment.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo

data class GeneralUsersItemPageViewState(
    private val users: UserInfo,
    val mElapsedTime: String
) {

    fun getUserName() = users.uName

    fun defaultPhotoVisibility() = if (users.uPhoto == "null") View.VISIBLE else View.GONE

    fun defaultPhoto(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.avatar)

    fun selectedPhotoVisibility() = if (users.uPhoto != "null") View.VISIBLE else View.GONE

    fun selectedPhoto() = users.uPhoto

    fun getStatus() = users.uStatu
}