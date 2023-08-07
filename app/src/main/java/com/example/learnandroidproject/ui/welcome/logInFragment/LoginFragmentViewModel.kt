package com.example.learnandroidproject.ui.welcome.logInFragment

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.common.isSuccess
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.LoginRequest
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import com.onesignal.OneSignal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LoginFragmentViewModel @Inject constructor(private val datingApiRepository: DatingApiRepository): BaseViewModel() {

    private val _logInPageViewStateLiveData: MutableLiveData<LogInPageViewState> = MutableLiveData()
    val logInPageViewStateLiveData: LiveData<LogInPageViewState> = _logInPageViewStateLiveData

    private val _errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val errorMessageLiveData: LiveData<String> = _errorMessageLiveData

    private val _loginStateLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val loginStateLiveData: LiveData<Boolean> = _loginStateLiveData

    init {
        fetchData()
    }

    fun fetchData(){
        _logInPageViewStateLiveData.value = LogInPageViewState()
    }
    fun postUserParams(user: LoginRequest,context: Context){
        isNetworkAvailable(context)

        val externalUserId = UUID.randomUUID().toString()
        OneSignal.setExternalUserId(externalUserId, object :OneSignal.OSExternalUserIdUpdateCompletionHandler{
            override fun onSuccess(p0: JSONObject?) {
                Log.e("OneSignal", "External user ID ayarlandı: $externalUserId")
            }

            override fun onFailure(p0: OneSignal.ExternalIdError?) {
                Log.e("OneSignal", "External user ID ayarlanamadı.")
            }

        } )

        val newUser = LoginRequest(user.userName,user.password,externalUserId)
        viewModelScope.launch(Dispatchers.IO){
            val uploadResult = datingApiRepository.login(newUser)
            val responseString = uploadResult.component1()?.string() ?: ""
            val accessTokenShared = context.getSharedPreferences("accessTokenShared", Context.MODE_PRIVATE)
            val refreshTokenShared = context.getSharedPreferences("RefShared", Context.MODE_PRIVATE)
            val loggedIdShared = context.getSharedPreferences("LoggedUserID",Context.MODE_PRIVATE)
            accessTokenShared.edit().remove("accessTokenKey").apply()
            refreshTokenShared.edit().remove("ref").apply()
            loggedIdShared.edit().remove("LoggedUserID").apply()

            if (uploadResult.isSuccess()) {
                // Parse the JSON response to extract the URL
                try {
                    val jsonObject = JSONObject(responseString)
                    val refToken = jsonObject.optString("refreshToken", "")
                    val accessToken = jsonObject.optString("accessToken", "")
                    val loggedUserId = jsonObject.optString("userId", "")

                    accessTokenShared.edit().putString("accessTokenKey", accessToken).apply()
                    refreshTokenShared.edit().putString("ref", refToken).apply()
                    loggedIdShared.edit().putString("LoggedUserId",loggedUserId).apply()

                    Log.e("Login access", "$accessToken")

                    _loginStateLiveData.postValue(true)

                } catch (e: JSONException) {
                    Log.e("JSONParsingError", "Hata Json Response'u Bölünemedi")
                    _loginStateLiveData.postValue(false)
                }
            } else {
                val error = uploadResult.component2()
                if (error != null && error is retrofit2.HttpException) {
                    if (error.code() == 401) {
                        _errorMessageLiveData.postValue("Giriş Başarısız. Kullanıcı Adı veya Şifre Hatalı.")
                    } else {
                        _errorMessageLiveData.postValue("Giriş Başarısız. Lütfen tekrar deneyin.")
                    }
                } else {
                    _errorMessageLiveData.postValue("Bağlantı hatası. Lütfen internetinizi kontrol edin.")
                }
                _loginStateLiveData.postValue(false)
            }
        }
    }
    fun checkFields(userName: String, password: String,context: Context) : Boolean {
        val userNamePattern = Regex("^[a-zA-Z0-9._%+-]+$")

        /*if (userNamePattern.matches(userName)){
            _errorMessageLiveData.value = "Kullanıcı Adı Özel Karakter İçeremez"
            return false //TODO username filtresini düzelt
        }else */if(userName.contains(" ")){
            _errorMessageLiveData.value = "Kullanıcı Adı Boşluk İçeremez"
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
        return true
    }

    private fun isNetworkAvailable(context: Context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        if (!(networkInfo != null && networkInfo.isConnected)){
            _errorMessageLiveData.postValue("Lütfen internet bağlantınızı kontrol edin")
        }
    }
}