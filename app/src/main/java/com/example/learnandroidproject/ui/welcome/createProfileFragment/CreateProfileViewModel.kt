package com.example.learnandroidproject.ui.welcome.createProfileFragment

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.common.isFailure
import com.example.learnandroidproject.common.isSuccess
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.LoginRequest
import com.example.learnandroidproject.data.local.model.dating.db.request.userRequest.User
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import com.onesignal.OneSignal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import javax.inject.Inject
@HiltViewModel
class CreateProfileViewModel@Inject constructor(private val datingApiRepository: DatingApiRepository): BaseViewModel() {

    private val _errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val errorMessageLiveData: LiveData<String> = _errorMessageLiveData

    private val _registerStateLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val registerStateLiveData: LiveData<Boolean> = _registerStateLiveData

    private val _loginStateLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val loginStateLiveData: LiveData<Boolean> = _loginStateLiveData

    fun checkFields(user: User): Boolean{

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
        return true
    }

    fun postUser(user: User, context: Context) {
        isNetworkAvailable(context)

        viewModelScope.launch(Dispatchers.IO) {
            val result = datingApiRepository.register(user)
            if (result.isFailure()){
                val errorMessage = "Bu kullanıcı adı veya e-posta zaten kayıtlı"
                _errorMessageLiveData.postValue(errorMessage)
                _registerStateLiveData.postValue(false)
            }else if (result.isSuccess()){
                _registerStateLiveData.postValue(true)
            }
        }
    }

    fun loginChatRooms(user: User,context: Context){

        isNetworkAvailable(context)

        val externalUserId = UUID.randomUUID().toString()
        OneSignal.setExternalUserId(externalUserId, object : OneSignal.OSExternalUserIdUpdateCompletionHandler{
            override fun onSuccess(p0: JSONObject?) {
                Log.e("OneSignal", "External user ID ayarlandı: $externalUserId")
            }
            override fun onFailure(p0: OneSignal.ExternalIdError?) {
                Log.e("OneSignal", "External user ID ayarlanamadı.")
            }
        } )


        val logUser = LoginRequest(user.userName.toString(),user.password.toString(),externalUserId)

        viewModelScope.launch(Dispatchers.IO){
            val result = datingApiRepository.login(logUser)
            val responseString = result.component1()?.string() ?: ""

            val accessTokenShared = context.getSharedPreferences("accessTokenShared",Context.MODE_PRIVATE)
            val refreshTokenShared = context.getSharedPreferences("RefShared", Context.MODE_PRIVATE)
            val loggedIdShared = context.getSharedPreferences("LoggedUserID",Context.MODE_PRIVATE)
            if (result.isSuccess()) {
                // Parse the JSON response to extract the URL
                try {
                    val jsonObject = JSONObject(responseString)
                    val refToken = jsonObject.optString("refreshToken", "")
                    val accessToken = jsonObject.optString("accessToken", "")
                    val loggedUserId = jsonObject.optString("userId", "")

                    accessTokenShared.edit().putString("accessTokenKey", accessToken).apply()
                    loggedIdShared.edit().putString("LoggedUserId",loggedUserId).apply()
                    refreshTokenShared.edit().putString("ref", refToken).apply()
                    Log.e("userId","$loggedUserId")
                    Log.e("Kayıt login access", "$accessToken")

                    _loginStateLiveData.postValue(true)

                } catch (e: JSONException) {
                    Log.e("JSONParsingError", "Hata Json Response'u Bölünemedi")
                    _loginStateLiveData.postValue(false)
                }
            } else{
                Log.e("giriş hatası","Hataa")
            }
        }
    }

    private fun isNetworkAvailable(context: Context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        if (!(networkInfo != null && networkInfo.isConnected)){
            _errorMessageLiveData.postValue("Lütfen internet bağlantınızı kontrol edin")
        }
    }
}