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

    fun defaultPhotoVisibility() = if (member.uPhoto == "null") View.VISIBLE else View.GONE

    fun userPhotoDefault(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.avatar)

    fun selectedPhotoVisibility() = if (member.uPhoto != "null") View.VISIBLE else View.GONE

    fun userPhoto() = member.uPhoto

    fun userName() = member.uName

    fun getMemberRole() = member.uRole

/*<ImageView
android:id="@+id/crossIcon"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
android:src="@{pageViewState.memberIcon(context)}"
android:layout_gravity="end|center"
android:layout_marginEnd="20dp"/>
fun cancelIcon(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.cross_icon)*/
}