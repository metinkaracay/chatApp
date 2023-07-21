package com.example.learnandroidproject.ui.welcome.popUpFragment

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R

class PopUpPageViewState {

    fun getHeaderText() = "Kayıt Başarılı!"

    fun Image(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.check_mark)

    fun getInfoText() = "Anasayfaya yönlendiriliyorsunuz"
}