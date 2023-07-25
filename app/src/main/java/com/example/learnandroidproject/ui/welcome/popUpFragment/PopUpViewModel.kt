package com.example.learnandroidproject.ui.welcome.popUpFragment

import android.content.Context
import android.net.Uri
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.common.isSuccess
import com.example.learnandroidproject.data.local.model.dating.db.request.userRequest.User
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*
import javax.inject.Inject
@HiltViewModel
class PopUpViewModel @Inject constructor(private val datingApiRepository: DatingApiRepository): BaseViewModel() {

    private val _popUpPageViewStateLiveData: MutableLiveData<PopUpPageViewState> = MutableLiveData()
    val popupPageViewStateLiveData: LiveData<PopUpPageViewState> = _popUpPageViewStateLiveData

    private val _popUpCountDownTimer: MutableLiveData<Int> = MutableLiveData()
    val popUpCountDownTimer: LiveData<Int> = _popUpCountDownTimer

    private val _uploadInProgress: MutableLiveData<Boolean> = MutableLiveData()
    val uploadInProgress: LiveData<Boolean> = _uploadInProgress

    private val _uploadMessage: MutableLiveData<String> = MutableLiveData()
    val uploadMessage: LiveData<String> = _uploadMessage

    fun decisionToFun(requestCode: Int){

        if (requestCode == 1){
            countDownTimer()
        }else if(requestCode == 2){
            photoPicker()
        }
    }

    fun countDownTimer(){
        _popUpPageViewStateLiveData.value = PopUpPageViewState(false)
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

    fun photoPicker(){
        _popUpPageViewStateLiveData.value = PopUpPageViewState(true)
    }

    fun postImage(selectedImage: Uri, context: Context) {
        val uuid = UUID.randomUUID()

        viewModelScope.launch(Dispatchers.IO) {
            selectedImage?.let { imageUri ->
                _uploadInProgress.postValue(true) // Yükleme başladı, true değeri gönderiliyor
                val imageStream = context.contentResolver.openInputStream(imageUri)
                imageStream?.use {
                    val byteArray = it.readBytes()
                    val imageBody = byteArray.toRequestBody("image/*".toMediaTypeOrNull())
                    val imagePart = MultipartBody.Part.createFormData("image", "${uuid}.jpg", imageBody)

                    // Yükleme işlemini gerçekleştir
                    val uploadResult = datingApiRepository.saveProfilePhoto(imagePart)

                    // Yükleme tamamlandı, false değeri gönderiliyor
                    _uploadInProgress.postValue(false)

                    // Uyarı mesajını belirle
                    if (uploadResult.isSuccess()) {
                        Log.e("yükleme durumu","başarılı")
                        _uploadMessage.postValue("Yükleme tamamlandı!") // Başarılı durum
                    } else {
                        Log.e("yükleme durumu","başarısız")
                        _uploadMessage.postValue("Yükleme başarısız oldu.") // Başarısız durum
                    }
                }
            }
        }
    }
}