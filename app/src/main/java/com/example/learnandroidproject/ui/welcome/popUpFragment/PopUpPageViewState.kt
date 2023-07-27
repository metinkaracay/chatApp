package com.example.learnandroidproject.ui.welcome.popUpFragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R

data class PopUpPageViewState(
    private val type: PopUpType,
    val photoUrl: String?
) {
    enum class PopUpType{
        SIGNIN,
        SELECTPHOTO,
        SHOWUSERPHOTO
    }
    fun defaultUserPhotoVisibility() = if (type == PopUpType.SELECTPHOTO || photoUrl == "null") View.VISIBLE else View.GONE
    fun selectedUserPhotoVisibility() = if (type == PopUpType.SHOWUSERPHOTO && photoUrl != "null") View.VISIBLE else View.GONE
    fun selectedUserPhotoUrl() = photoUrl
    fun userPhotoSaveButton() = if (type == PopUpType.SELECTPHOTO) View.VISIBLE else View.GONE
    fun headerTextVisibility() = if (type == PopUpType.SIGNIN) View.VISIBLE else View.GONE
    fun getHeaderText() = "Kayıt Başarılı!"
    fun imageVisibility() = if (type == PopUpType.SIGNIN) View.VISIBLE else View.GONE
    fun Image(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.check_mark)
    fun infoTextVisibility() = if (type == PopUpType.SIGNIN) View.VISIBLE else View.GONE
    fun getInfoText() = "Anasayfaya yönlendiriliyorsunuz"
}