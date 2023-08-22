package com.example.learnandroidproject.ui.welcome.groupChattingFragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.dp
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
    var userPhoto: List<String> = arrayListOf(),
    var loggedUserRank: Int = 4,
    var membersNameList: MutableMap<Int,String>
) {
    fun loadingScreenVisibility() = if (!isLoaded) View.VISIBLE else View.GONE

    fun backArrow(context: Context) : Drawable? = ContextCompat.getDrawable(context, R.drawable.back_arrow_icon)
    fun selectedPhoto() = group.groupPhoto
    fun getGroupName() = if (group.groupName.length > 10){
        group.groupName.substring(0,10)+"..."
    }else{
        group.groupName
    }

    fun editTextHint() = "Mesaj"
    fun groupVisibility() = if (isRaceStart) View.GONE else View.VISIBLE
    fun sendButtonBackground(context: Context): Int = ContextCompat.getColor(context, R.color.send_message_color)
    fun sendIcon(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.send_icon)
    fun adminButtonVisibility(): Int {
        return if (isAdmin && !isRaceStart) View.VISIBLE else View.GONE
    }
    fun timerVisibility() = if (!isRaceStart) View.GONE else View.VISIBLE
    fun getTimer() = remainingTime
    fun setTimerPopUp() = if(!popUpVisibility) View.GONE else View.VISIBLE
    fun raceAnimation() = if (!isRaceStart) View.GONE else View.VISIBLE

    fun userPhoto1(): String {
        return if (userPhoto.isNotEmpty()) {
            userPhoto[0]
        } else {
            "null"
        }
    }
    fun userPhoto2(): String {
        return if (userPhoto.isNotEmpty()) {
            userPhoto[1]
        } else {
            "null"
        }
    }
    fun userPhoto3(): String {
        return if (userPhoto.isNotEmpty()) {
            userPhoto[2]
        } else {
            "null"
        }
    }
    fun getUserStroke1() = if (loggedUserRank == 1) 2.dp else 0.dp
    fun getUserStroke2() = if (loggedUserRank == 2) 2.dp else 0.dp
    fun getUserStroke3() = if (loggedUserRank == 3) 2.dp else 0.dp
}