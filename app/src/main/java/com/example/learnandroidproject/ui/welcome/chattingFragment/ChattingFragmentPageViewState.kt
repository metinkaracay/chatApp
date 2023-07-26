package com.example.learnandroidproject.ui.welcome.chattingFragment

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R

class ChattingFragmentPageViewState {


    fun userPicture(context:Context) : Drawable? = ContextCompat.getDrawable(context, R.drawable.avatar)

    fun getUserName() = "Deneme"

    fun editTextHint() = "Mesaj"
}