package com.example.learnandroidproject.ui.welcome.groupChatsFragment.adapter

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupInfo

data class GroupsItemPageViewState(
    private val group: GroupInfo,
    val messageElapsedTime: String
) {

    fun getUserName() = group.groupName

    fun defaultPhotoVisibility() = if (group.groupPhoto == "null") View.VISIBLE else View.GONE

    fun defaultPhoto(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.avatar)

    fun selectedPhotoVisibility() = if (group.groupPhoto != "null") View.VISIBLE else View.GONE

    fun selectedPhoto() = group.groupPhoto

    fun getLastMessage() = group.lastMessage

    fun lastMessageFont(context: Context): Typeface? = if (group.isSeen == false) ResourcesCompat.getFont(context,
        R.font.montserrat_bold) else ResourcesCompat.getFont(context, R.font.montserrat_regular)

    fun getElapsedTime() = messageElapsedTime

    fun eventStateVisibility() = if (!group.isEvent) View.GONE else View.VISIBLE

    fun raceIcon(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.race_flag_icon)
}