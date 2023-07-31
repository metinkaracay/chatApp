package com.example.learnandroidproject.ui.welcome.mainFragment

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R

class MainFragmentPageViewState {

    fun signInButtonColor(context: Context): Int = ContextCompat.getColor(context,R.color.online_color)
    fun logInButtonColor(context: Context): Int = ContextCompat.getColor(context,R.color.send_Button)
    fun textColor(context: Context) = ContextCompat.getColor(context, R.color.white)
    fun getLogInText() = "Giriş Yap"
    fun getSignInText() = "Kayıt Ol"
}
