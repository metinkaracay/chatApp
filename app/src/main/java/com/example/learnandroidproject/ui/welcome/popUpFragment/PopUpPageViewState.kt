package com.example.learnandroidproject.ui.welcome.popUpFragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.example.learnandroidproject.R

data class PopUpPageViewState(
    val photoPick: Boolean
) {

    fun defaultUserPhotoVisibility() = if (photoPick) View.VISIBLE else View.GONE

    //fun selectedUserPhotoVisibility() = if (photoPick && photo != null) View.VISIBLE else View.GONE TODO daha önceden seçilen fotoyu açılan popupta göstermek için gerekir

    //fun selectedUserPhoto() = photo

    //fun getUserPhoto(context: Context) : Drawable? =
    fun userPhotoSaveButton() = if (photoPick) View.VISIBLE else View.GONE

    fun headerTextVisibility() = if (!photoPick) View.VISIBLE else View.GONE
    fun getHeaderText() = "Kayıt Başarılı!"

    fun imageVisibility() = if (!photoPick) View.VISIBLE else View.GONE

    fun Image(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.check_mark)

    fun infoTextVisibility() = if (!photoPick) View.VISIBLE else View.GONE

    fun getInfoText() = "Anasayfaya yönlendiriliyorsunuz"
}