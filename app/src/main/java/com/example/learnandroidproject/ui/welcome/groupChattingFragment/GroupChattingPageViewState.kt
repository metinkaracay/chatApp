package com.example.learnandroidproject.ui.welcome.groupChattingFragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem

data class GroupChattingPageViewState(
    val group: GroupInfo,
    val messages: List<MessageItem>,
    var isAdmin: Boolean,
    var isRaceStart: Boolean,
    var remainingTime: String = "",
    var popUpVisibility: Boolean = false,
    var users: List<String> = arrayListOf()
) {
    fun backArrow(context: Context) : Drawable? = ContextCompat.getDrawable(context, R.drawable.back_arrow_icon)

    fun defaultPhotoVisibility() = if (group.groupPhoto == "null") View.VISIBLE else View.GONE

    fun defaultPhoto(context: Context) : Drawable? = ContextCompat.getDrawable(context, R.drawable.avatar)

    fun selectedPhotoVisibility() = if (group.groupPhoto != "null") View.VISIBLE else View.GONE

    fun selectedPhoto() = group.groupPhoto

    fun getUserName() = group.groupName

    fun editTextHint() = "Mesaj"
    fun groupVisibility() = if (isRaceStart) View.GONE else View.VISIBLE
    fun sendButtonBackground(context: Context): Int = ContextCompat.getColor(context, R.color.send_message_color)
    fun sendIcon(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.send_icon)
    fun adminButtonVisibility() = if (isAdmin && !isRaceStart) View.VISIBLE else View.GONE
    fun timerVisibility() = if (isRaceStart) View.VISIBLE else View.GONE
    fun getTimer() = remainingTime
    fun setTimerPopUp() = if(popUpVisibility) View.VISIBLE else View.GONE
    fun raceAnimation() = if (isRaceStart) View.VISIBLE else View.GONE

    //fun getUser1Name() = users[0]
    fun getUser1Name(): String {
        return if (users.isNotEmpty()) {
            users[0]
        } else {
            "User1"
        }
    }
    //fun getUser2Name() = users[1]
    fun getUser2Name(): String {
        return if (users.isNotEmpty()) {
            users[1]
        } else {
            "User2"
        }
    }
    //fun getUser3Name() = users[2]
    fun getUser3Name(): String {
        return if (users.isNotEmpty()) {
            users[2]
        } else {
            "User3"
        }
    }
    fun userPhoto1(): String {
        return if (users.isNotEmpty()) {
            users[0]
        } else {
            "null"
        }
    }
    fun userPhoto2(): String {
        return if (users.isNotEmpty()) {
            users[1]
        } else {
            "null"
        }
    }
    fun userPhoto3(): String {
        return if (users.isNotEmpty()) {
            users[2]
        } else {
            "null"
        }
    }
}