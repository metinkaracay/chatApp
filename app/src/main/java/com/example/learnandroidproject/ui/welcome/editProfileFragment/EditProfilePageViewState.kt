package com.example.learnandroidproject.ui.welcome.editProfileFragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.request.userRequest.User

data class EditProfilePageViewState(
    val user: User,
    val isEditting: Boolean
) {

    fun toolbarColor(context: Context): Int = ContextCompat.getColor(context,R.color.toolbar_color)
    fun backArrow(context: Context) : Drawable? = ContextCompat.getDrawable(context, R.drawable.back_arrow_icon)
    fun getHeaderName() = user.userName
    fun getSaveButtonText() = "Kaydet"
    fun saveButtonColor(context: Context): Int = if (isEditting) ContextCompat.getColor(context,R.color.online_color) else ContextCompat.getColor(context, R.color.grey)
    fun getEditButtonText() = "Düzenle"
    fun editButtonColor(context: Context): Int = if (isEditting) ContextCompat.getColor(context,R.color.grey) else ContextCompat.getColor(context, R.color.online_color)
    fun getUserPhoto() = user.photo
    fun getStatus() = if (user.status.isNullOrEmpty()) "" else user.status
    fun getFirstName() = if (user.firstName.isNullOrEmpty()) "" else user.firstName
    fun getLastName() = if (user.lastName.isNullOrEmpty()) "" else user.lastName
    fun getAge() = if (user.age.isNullOrEmpty()) "" else user.age

}