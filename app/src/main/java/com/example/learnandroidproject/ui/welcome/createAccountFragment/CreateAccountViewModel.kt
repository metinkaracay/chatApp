package com.example.learnandroidproject.ui.welcome.createAccountFragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.learnandroidproject.ui.base.BaseViewModel

class CreateAccountViewModel: BaseViewModel() {

    private val _createAccountLiveData: MutableLiveData<String> = MutableLiveData()
    val createAccountLiveData: LiveData<String> = _createAccountLiveData

    fun checkFields(userName: String, email: String, password: String,context: Context): Boolean{

        if (email.isNotEmpty() && password.isNotEmpty() && userName.isNotEmpty()){
            Log.e("SignInFragment", "userName: $userName, Email: $email, Şifre: $password")
            return true
        } else {
            _createAccountLiveData.value = "Lütfen Tüm Alanları Doldurun"
            return false
        }

    }
}