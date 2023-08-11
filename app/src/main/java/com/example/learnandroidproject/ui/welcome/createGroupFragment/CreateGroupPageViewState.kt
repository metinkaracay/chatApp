package com.example.learnandroidproject.ui.welcome.createGroupFragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo

data class CreateGroupPageViewState(
    val users: List<UserInfo>,
    val isUserSelect: Boolean
) {

    fun toolbarColor(context: Context): Int = ContextCompat.getColor(context, R.color.toolbar_color)

    fun backArrow(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.back_arrow_icon)

    fun getHeader() = "Chat App"

    fun nextButtonVisibility() = if (!isUserSelect) View.VISIBLE else View.GONE // TODO view model düzelince değişecek

    fun nextButtonColor(context: Context): Int = ContextCompat.getColor(context, R.color.online_color)

    fun nextButtonIcon(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.send_icon)

    init {
        Log.e("gelen bool","$isUserSelect")
    }
}