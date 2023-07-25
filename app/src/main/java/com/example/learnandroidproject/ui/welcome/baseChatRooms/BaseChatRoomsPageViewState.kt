package com.example.learnandroidproject.ui.welcome.baseChatRooms

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R

class BaseChatRoomsPageViewState(
    val url: String
) {

    fun defaultPhotoVisibility() = if (url == "null") View.VISIBLE else View.GONE

    fun defaultProfilePhoto(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.avatar)

    fun selectedPhotoVisibility() = if (url != "null") View.VISIBLE else View.GONE

    fun selectedProfilePhoto() = url
}