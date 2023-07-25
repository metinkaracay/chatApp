package com.example.learnandroidproject.ui.welcome.editProfileFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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

    private val _updateProfilePhotoLiveData: MutableLiveData<String> = MutableLiveData()
    val updateProfilePhotoLiveData: LiveData<String> = _updateProfilePhotoLiveData

    init {
        fetchUserData()
    }

    fun fetchUserData(){
        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.fetchUserData().get()?.let {
                withContext(Dispatchers.Main){
                    _editProfilePageViewStateLiveData.value = EditProfilePageViewState(it,null)
                }
            }
        }
    }

    fun updateToPhoto(link: String){
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000L)
            withContext(Dispatchers.Main) {
                _editProfilePageViewStateLiveData.value = editProfilePageViewStateLiveData.value?.copy(image = link)
            }
        }
    }
}