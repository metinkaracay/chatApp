package com.example.learnandroidproject.ui.welcome.editProfileFragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R
import com.example.learnandroidproject.data.local.model.dating.db.request.userRequest.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

data class EditProfilePageViewState(
    private val user: User,
    val image: String?,
    val isEditting: Boolean
) {

    fun getSaveButtonText() = "Kaydet"
    fun saveButtonColor(context: Context): Int = if (isEditting) ContextCompat.getColor(context,R.color.online_color) else ContextCompat.getColor(context, R.color.grey)
    fun getEditButtonText() = "Düzenle"
    fun editButtonColor(context: Context): Int = if (isEditting) ContextCompat.getColor(context,R.color.grey) else ContextCompat.getColor(context, R.color.online_color)
    fun defaultPhoto() = if (user.photo == "null") View.VISIBLE else View.GONE
    fun userDefaultPhoto(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.avatar)

    fun selectedPhoto() = if (user.photo != "null") View.VISIBLE else View.GONE

    fun getUserPhoto() = if (image != null ){
        Log.e("çalıştı","çalıştışimdi $image")
        image
    }else{
        user.photo
    }
    fun getStatus() = if (user.status.isNullOrEmpty()) "" else user.status

    fun getFirstName() = if (user.firstName.isNullOrEmpty()) "" else user.firstName

    fun getLastName() = if (user.lastName.isNullOrEmpty()) "" else user.lastName

    fun getAge() = if (user.age.isNullOrEmpty()) "" else user.age

}