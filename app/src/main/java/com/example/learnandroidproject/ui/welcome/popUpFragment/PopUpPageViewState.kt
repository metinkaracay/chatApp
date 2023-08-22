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
        SHOWUSERPHOTO,
        LOGOUT
    }
    fun selectedUserPhotoVisibility() = if ((type == PopUpType.SELECTPHOTO) || (type == PopUpType.SHOWUSERPHOTO )) View.VISIBLE else View.GONE
    fun selectedUserPhotoUrl() = photoUrl
    fun selectPhotoButtons() = if (type == PopUpType.SELECTPHOTO) View.VISIBLE else View.GONE
    fun userPhotoSaveButton() = if (type == PopUpType.SELECTPHOTO) View.VISIBLE else View.GONE // Yakında silinecek
    fun getSaveButtonText() = "Kaydet"
    fun popUpCloseButton() = if (type == PopUpType.SELECTPHOTO) View.VISIBLE else View.GONE // yakında silinecek
    fun getCloseButtonText() = "Kapat"
    fun headerTextVisibility() = if (type == PopUpType.SIGNIN || type == PopUpType.LOGOUT) View.VISIBLE else View.GONE
    fun getHeaderText() = if (type == PopUpType.SIGNIN) "Kayıt Başarılı!" else "Çıkmak İstiyor Musunuz?"
    fun imageVisibility() = if (type == PopUpType.SIGNIN) View.VISIBLE else View.GONE
    fun Image(context: Context): Drawable? = ContextCompat.getDrawable(context, R.drawable.check_mark)
    fun logoutOptionsVisibility() = if (type == PopUpType.LOGOUT) View.VISIBLE else View.GONE
    fun getLogoutRejectButtonText() = "Hayır"
    fun getLogoutAcceptButtonText() = "Evet"
    fun infoTextVisibility() = if (type == PopUpType.SIGNIN) View.VISIBLE else View.GONE
    fun getInfoText() = "Anasayfaya yönlendiriliyorsunuz"
}