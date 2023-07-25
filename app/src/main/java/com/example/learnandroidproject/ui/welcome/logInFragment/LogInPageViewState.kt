package com.example.learnandroidproject.ui.welcome.logInFragment

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R

class LogInPageViewState {

    fun getCreateAccount() = "Hesap Oluşturmak İstiyorum"

    fun getButtonText() = "Giriş Yap"

    fun buttonTextColor(context: Context): Int = ContextCompat.getColor(context,R.color.white)
}