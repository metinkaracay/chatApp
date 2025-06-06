package com.example.learnandroidproject.ui.welcome.editProfileFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.UpdateUser
import com.example.learnandroidproject.data.local.model.dating.db.request.userRequest.User
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import com.github.michaelbull.result.get
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val datingApiRepository: DatingApiRepository): BaseViewModel() {

    private val _editProfilePageViewStateLiveData: MutableLiveData<EditProfilePageViewState> = MutableLiveData()
    val editProfilePageViewStateLiveData: LiveData<EditProfilePageViewState> = _editProfilePageViewStateLiveData

    private val _errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val errorMessageLiveData: LiveData<String> = _errorMessageLiveData

    private val _userPhotoLiveData: MutableLiveData<String> = MutableLiveData()
    val userPhotoLiveData: LiveData<String> = _userPhotoLiveData

    var currentUser = User(null,null,null,null,null,null,null,null,null)
    init {
        fetchUserData()
    }

    fun fetchUserData(){
        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.fetchUserData().get()?.let {
                withContext(Dispatchers.Main){
                    currentUser = it
                    _editProfilePageViewStateLiveData.value = EditProfilePageViewState(it,false)
                    // Açılan popUp'a kullanıcının fotoğrafını gönderir
                    if (it.photo != null){
                        _userPhotoLiveData.value = it.photo!!
                    }else{
                        _userPhotoLiveData.value = "null"
                    }
                }
            }
        }
    }
    fun updateToPhoto(link: String){
        viewModelScope.launch(Dispatchers.IO) {
            delay(1500L)
            withContext(Dispatchers.Main) {
                _userPhotoLiveData.value = link
                currentUser.photo = link
                _editProfilePageViewStateLiveData.value = editProfilePageViewStateLiveData.value?.copy(user = currentUser)
            }
        }
    }
    fun editButtonListener(isChanged: Boolean){
        _editProfilePageViewStateLiveData.value = _editProfilePageViewStateLiveData.value?.copy(isEditting = isChanged)
    }
    fun checkFields(user: UpdateUser): Boolean{
        val namesPattern = Regex("^[a-zA-ZÇçĞğİıÖöŞşÜü]+( [a-zA-ZÇçĞğİıÖöŞşÜü]+)*$")

        val userFields = listOf(
            user.uFirstName to "Ad",
            user.uLastName to "Soyad",
            user.uStatus to "Durum"
        )

        for ((field, fieldName) in userFields) {
            if (field.isNullOrEmpty()) {
                _errorMessageLiveData.value = "$fieldName Boş Bırakılamaz"
                return false
            }
            if (field.matches(namesPattern) == false && field != user.uStatus){
                _errorMessageLiveData.value = "$fieldName Sadece Alfabetik Karakterler İçerebilir"
                return false
            }
        }

        if (user.uAge.isNullOrEmpty() || user.uAge.toInt() !in 15..100) {
            _errorMessageLiveData.value = "Geçerli Bir Yaş Giriniz"
            return false
        }
        return true
    }
    fun patchChangedUserFields(user: UpdateUser){
        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.updateProfile(user)

            fetchUserData()
        }
    }
}