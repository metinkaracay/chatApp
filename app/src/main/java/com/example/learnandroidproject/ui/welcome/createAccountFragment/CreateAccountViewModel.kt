package com.example.learnandroidproject.ui.welcome.createAccountFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.learnandroidproject.ui.base.BaseViewModel

class CreateAccountViewModel: BaseViewModel() {

    private val _createAccountLiveData: MutableLiveData<String> = MutableLiveData()
    val createAccountLiveData: LiveData<String> = _createAccountLiveData

    fun checkFields(userName: String, email: String, password: String): Boolean{

        if (email.isNotEmpty() && password.isNotEmpty() && userName.isNotEmpty()){
            val emailPattern = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
            if(emailPattern.matches(email)){
                if (password.length >= 6) {
                    return true
                } else {
                    _createAccountLiveData.value = "Şifre minimum 6 karakter olmalıdır"
                    return false
                }
            }else{
                _createAccountLiveData.value = "Lütfen Geçerli Bir E-Posta Giriniz"
                return false
            }
        } else {
            _createAccountLiveData.value = "Lütfen Tüm Alanları Doldurun"
            return false
        }
    }
}