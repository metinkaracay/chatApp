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
import javax.inject.Inject
@HiltViewModel
class CreateProfileViewModel@Inject constructor(private val datingApiRepository: DatingApiRepository): BaseViewModel() {

    private val _errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val errorMessageLiveData: LiveData<String> = _errorMessageLiveData

    fun checkMessage(user: User): Boolean{

        val userFields = listOf(
            user.firstName to "Ad",
            user.lastName to "Soyad",
            user.gender to "Cinsiyet",
            user.age to "Yaş"
        )

        for ((field, fieldName) in userFields) {
            if (field.isNullOrEmpty()) {
                _errorMessageLiveData.value = "$fieldName Boş Bırakılamaz"
                return false
            }
        }

        if (user.age!!.toInt() !in 15..100) {
            _errorMessageLiveData.value = "Geçerli Bir Yaş Giriniz"
            return false
        }
        return postUser(user)
    }

    fun postUser(user: User): Boolean {

        viewModelScope.launch(Dispatchers.IO) {
            datingApiRepository.register(user)
        }
        return true
    }
}