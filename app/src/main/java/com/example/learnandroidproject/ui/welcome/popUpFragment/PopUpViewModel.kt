package com.example.learnandroidproject.ui.welcome.popUpFragment

import android.content.Context
import android.net.Uri
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.common.isSuccess
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import javax.inject.Inject
@HiltViewModel
class PopUpViewModel @Inject constructor(private val datingApiRepository: DatingApiRepository): BaseViewModel() {

    private val _popUpPageViewStateLiveData: MutableLiveData<PopUpPageViewState> = MutableLiveData()
    val popupPageViewStateLiveData: LiveData<PopUpPageViewState> = _popUpPageViewStateLiveData

    private val _popUpCountDownTimer: MutableLiveData<Int> = MutableLiveData()
    val popUpCountDownTimer: LiveData<Int> = _popUpCountDownTimer

    private val _uploadResponse: MutableLiveData<String> = MutableLiveData()
    val uploadResponse: LiveData<String> = _uploadResponse

    fun decisionToFun(requestCode: Int,imageUrl: String?){
        if (requestCode == 1){
            countDownTimer()
        }else if(requestCode == 2){
            photoPicker(imageUrl)
        }else if(requestCode == 3){
            showUserPhoto(imageUrl)
        }
    }

    fun countDownTimer(){
        _popUpPageViewStateLiveData.value = PopUpPageViewState(PopUpPageViewState.PopUpType.SIGNIN,null)
        val timer: Long = 4
        val countDownInterval: Long = 1000

        object : CountDownTimer(timer * 1000, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = (millisUntilFinished / 1000).toInt()
                _popUpCountDownTimer.value = secondsRemaining
            }

            override fun onFinish() {
                _popUpCountDownTimer.value = 0
            }
        }.start()
    }

    fun photoPicker(imageUrl: String?){
        _popUpPageViewStateLiveData.value = PopUpPageViewState(PopUpPageViewState.PopUpType.SELECTPHOTO,imageUrl)
    }
    fun showUserPhoto(imageUrl: String?){
        _popUpPageViewStateLiveData.value = PopUpPageViewState(PopUpPageViewState.PopUpType.SHOWUSERPHOTO,imageUrl)
    }
    fun postImage(selectedImage: Uri, context: Context){
        val uuid = UUID.randomUUID()

        viewModelScope.launch(Dispatchers.IO) {
            selectedImage?.let { imageUri ->
                val imageStream = context.contentResolver.openInputStream(imageUri)
                imageStream?.use {
                    val byteArray = it.readBytes()
                    val imageBody = byteArray.toRequestBody("image/*".toMediaTypeOrNull())
                    val imagePart = MultipartBody.Part.createFormData("image", "${uuid}.jpg", imageBody)

                    // Yükleme işlemini gerçekleştir
                    val uploadResult = datingApiRepository.saveProfilePhoto(imagePart)

                    if (uploadResult.isSuccess()) {
                        val responseString = uploadResult.component1()?.string() ?: ""
                        Log.e("responseString", responseString)

                        // Parse the JSON response to extract the URL
                        try {
                            val jsonObject = JSONObject(responseString)
                            val url = jsonObject.optString("url", "")
                            Log.e("imageURL", url)
                            _uploadResponse.postValue(url)
                        } catch (e: JSONException) {
                            Log.e("JSONParsingError", "Error parsing response JSON")
                        }
                    } else {
                        Log.e("yükleme durumu", "başarısız")
                    }
                }
            }
        }
    }
}