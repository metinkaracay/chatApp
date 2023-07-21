package com.example.learnandroidproject.ui.welcome.testImageFragment

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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
class TestImageViewModel@Inject constructor(private val datingApiRepository: DatingApiRepository): BaseViewModel() {

    private val _testImagePageViewStateLiveData: MutableLiveData<PageViewState> = MutableLiveData()

    val testImagePageViewStateLiveData: LiveData<PageViewState> = _testImagePageViewStateLiveData


    init {


    }

    private fun fetchData(){
        /*viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.test().get()?.let {
                withContext(Dispatchers.Main){
                    _testImagePageViewStateLiveData.value = PageViewState()
                }
            }
        }*/
        _testImagePageViewStateLiveData.value = PageViewState()
    }

    fun postImage(selectedImage: Uri, context: Context) {
        val uuid = UUID.randomUUID()
        val user: User = User("username","email.com","147852","Metin","Karaçay", "23","Erkek")

        viewModelScope.launch(Dispatchers.IO) {
            selectedImage?.let { imageUri ->
                val imageStream = context.contentResolver.openInputStream(imageUri)
                imageStream?.use {
                    val byteArray = it.readBytes()
                    val imageBody = byteArray.toRequestBody("image/*".toMediaTypeOrNull())
                    val imagePart = MultipartBody.Part.createFormData("test", "${uuid}.jpg", imageBody)

                    datingApiRepository.test(imagePart)
                }
            }
        }
    }

    fun postMethod(selectedImage: Uri, context: Context, user2: com.example.learnandroidproject.data.local.model.dating.db.request.userRequest.User) {
        val uuid = UUID.randomUUID()

        viewModelScope.launch(Dispatchers.IO) {
            selectedImage?.let { imageUri ->
                val imageStream = context.contentResolver.openInputStream(imageUri)
                imageStream?.use {
                    val byteArray = it.readBytes()
                    val imageBody = byteArray.toRequestBody("image/*".toMediaTypeOrNull())
                    val imagePart = MultipartBody.Part.createFormData("test", "${uuid}.jpg", imageBody)
                    Log.e("test","çalıştı")
                    Log.e("tset","${user2.email}")
                    val result = datingApiRepository.test(imagePart)
                }
            }
        }
    }

}