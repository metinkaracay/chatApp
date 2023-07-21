package com.example.learnandroidproject.ui.welcome.createProfileFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.data.local.model.dating.db.request.userRequest.User
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
@HiltViewModel
class CreateProfileViewModel@Inject constructor(private val datingApiRepository: DatingApiRepository): BaseViewModel() {

    private val _errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val errorMessageLiveData: LiveData<String> = _errorMessageLiveData

    val user: User = User("username","email.com","147852","Metin","Karaçay","23","Erkek")

    fun checkMessage2(user: User){

        val userFields = listOf(
            user.userName to "Kullanıcı Adı",
            user.email to "E-posta",
            user.password to "Şifre",
            user.firstName to "Ad",
            user.lastName to "Soyad",
            user.gender to "Cinsiyet",
            user.age to "Yaş"
        )

        for ((field, fieldName) in userFields) {
            if (field.isNullOrEmpty()) {
                _errorMessageLiveData.value = "$fieldName Boş Bırakılamaz"
                return
            }
        }

        if (user.age.isNullOrEmpty() || user.age!!.toInt() !in 1..100) {
            _errorMessageLiveData.value = "Geçerli Bir Yaş Giriniz"
            return
        }

        postUser2(user)
    }

    fun postUser2(user: User) {

        viewModelScope.launch(Dispatchers.IO) {
            datingApiRepository.register(user)
            }
    }
}