package com.example.learnandroidproject.ui.welcome.groupChattingFragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem

data class GroupChattingPageViewState(
    val group: GroupInfo,
    val messages: List<MessageItem>,
    var isAdmin: Boolean = false,
    var isRaceStart: Boolean = false,
    var remainingTime: String = "",
    var popUpVisibility: Boolean = false,
    var isLoaded: Boolean = false,
    var users: List<String> = arrayListOf()
) {
    fun loadingScreenVisibility() = if (!isLoaded) View.VISIBLE else View.GONE

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
    fun adminButtonVisibility(): Int {
        val test = (isAdmin && !isRaceStart)
        Log.e("test_visibility", "$test")
        return if (isAdmin && !isRaceStart) View.VISIBLE else View.GONE
    }
    fun timerVisibility() = if (!isRaceStart) View.GONE else View.VISIBLE
    fun getTimer() = remainingTime
    fun setTimerPopUp() = if(!popUpVisibility) View.GONE else View.VISIBLE
    fun raceAnimation() = if (!isRaceStart) View.GONE else View.VISIBLE

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