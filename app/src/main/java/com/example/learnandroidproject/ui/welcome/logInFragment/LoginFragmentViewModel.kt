package com.example.learnandroidproject.ui.welcome.logInFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.common.isSuccess
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.LoginRequest
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class LoginFragmentViewModel @Inject constructor(private val datingApiRepository: DatingApiRepository): BaseViewModel() {

    private val _logInPageViewStateLiveData: MutableLiveData<LogInPageViewState> = MutableLiveData()
    val logInPageViewStateLiveData: LiveData<LogInPageViewState> = _logInPageViewStateLiveData

    private val _errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val errorMessageLiveData: LiveData<String> = _errorMessageLiveData

    private val _token: MutableLiveData<String> = MutableLiveData()
    val token: LiveData<String> = _token

    private val _uploadResponse: MutableLiveData<String> = MutableLiveData()
    val uploadResponse: LiveData<String> = _uploadResponse
    init {
        fetchData()
    }

    fun fetchData(){
        _logInPageViewStateLiveData.value = LogInPageViewState()
    }

    fun postUserParams(user: LoginRequest): Boolean{
        viewModelScope.launch(Dispatchers.IO){
            val uploadResult = datingApiRepository.login(user)
            val responseString = uploadResult.component1()?.string() ?: ""
            Log.e("responseString", responseString)
            if (uploadResult.isSuccess()) {

                // Parse the JSON response to extract the URL
                try {
                    val jsonObject = JSONObject(responseString)
                    val refToken = jsonObject.optString("refreshToken", "")
                    val accessToken = jsonObject.optString("accessToken", "")

                    _token.postValue(accessToken)

                } catch (e: JSONException) {
                    Log.e("JSONParsingError", "Error parsing response JSON")
                }
            } else {
                Log.e("yükleme durumu", "başarısız")
            }

        }
        return true
    }

    fun checkFields(userName: String, password: String) : Boolean {
        val userNamePattern = Regex("[a-zA-Z0-9._%+-]")

        if (userNamePattern.matches(userName)){
            _errorMessageLiveData.value = "Kullanıcı Adı Özel Karakter İçeremez"
            return false
        }else if (userName.isNullOrEmpty()) {
            _errorMessageLiveData.value = "Kullanıcı Adı Boş Bırakılamaz"
            return false
        }else if (password.isNullOrEmpty()){
            _errorMessageLiveData.value = "Şifre Boş Bırakılamaz"
            return false
        }else if (password.length < 6){
            _errorMessageLiveData.value = "Şifre En Az 6 Haneli Olabilir"
            return false
        }
        val user = LoginRequest(userName,password)
        return postUserParams(user)
    }
}