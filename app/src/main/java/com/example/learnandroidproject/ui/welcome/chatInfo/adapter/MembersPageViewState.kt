package com.example.learnandroidproject.ui.welcome.chatInfo.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupMember

data class MembersPageViewState(
    val member: GroupMember
) {
    fun selectedPhotoVisibility() = View.VISIBLE

    fun userPhoto() = member.uPhoto

    fun userName() = member.uName

    fun getMemberRole() = member.uRole
}