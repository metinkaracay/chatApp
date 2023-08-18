package com.example.learnandroidproject.ui.welcome.groupChatsFragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupInfo

data class GroupChatsPageViewState(
    val groups: List<GroupInfo>
) {

    fun emptyListLogoVisibility() = if (groups.isEmpty()) View.VISIBLE else View.GONE
    fun emptyListLogo(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.empty_friend_list_logo)
    fun emptyListInfoVisibility() = if (groups.isEmpty()) View.VISIBLE else View.GONE
    fun getEmptyListInfo() = "Henüz Kimse ile Mesajlaşmadınız"
}