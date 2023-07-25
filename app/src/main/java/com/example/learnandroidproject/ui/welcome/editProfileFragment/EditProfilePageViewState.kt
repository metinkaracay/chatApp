package com.example.learnandroidproject.ui.welcome.editProfileFragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.request.userRequest.User

data class EditProfilePageViewState(
    private val user: User,
    val image: String?
) {

    fun defaultPhoto() = if (user.photo == "null") View.VISIBLE else View.GONE
    fun userDefaultPhoto(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.avatar)

    fun selectedPhoto() = if (user.photo != "null") View.VISIBLE else View.GONE

    fun getUserPhoto() = user.photo
    //fun getUserPhoto() = if (image != null ) Log.e("geldi","$image") else user.photo TODO Response ile image linki aldığın zaman log yerine linki ver çalışacaktır. Şuan uri geliyor
    fun getStatus() = if (user.status.isNullOrEmpty()) "" else user.status

    fun getFirstName() = if (user.firstName.isNullOrEmpty()) "" else user.firstName

    fun getLastName() = if (user.lastName.isNullOrEmpty()) "" else user.lastName

    fun getAge() = if (user.age.isNullOrEmpty()) "" else user.age

}