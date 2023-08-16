package com.example.learnandroidproject.ui.welcome.chatInfo

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupMember

class ChatInfoPageViewState(
    val group: GroupInfo,
    val groupMembers: List<GroupMember>
) {
    fun toolbarColor(context: Context): Int = ContextCompat.getColor(context, R.color.toolbar_color)

    fun backArrow(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.back_arrow_icon)

    fun getHeader() = "Chat App"

    fun defaultPhotoVisibility() = if (group.groupPhoto == "null") View.VISIBLE else View.GONE

    fun userDefaultPhoto(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.avatar)

    fun selectedPhotoVisibility() = if (group.groupPhoto != "null") View.VISIBLE else View.GONE

    fun selectedPhoto() = group.groupPhoto

    fun getUserName() = group.groupName//user.firstName+" "+user.lastName

    fun getAgeHeader() = "Yaş: "

    fun getUserAge() = "2"//user.age

    fun getStatusHeader() = "Durumu: "

    fun getUserStatus() = "durum" //user.status
}