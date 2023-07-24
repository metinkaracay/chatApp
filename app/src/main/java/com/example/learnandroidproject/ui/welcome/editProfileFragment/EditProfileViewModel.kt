package com.example.learnandroidproject.ui.welcome.editProfileFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.learnandroidproject.ui.base.BaseViewModel

class EditProfileViewModel : BaseViewModel() {

    private val _editProfilePageViewStateLiveData: MutableLiveData<EditProfilePageViewState> = MutableLiveData()
    val editProfilePageViewStateLiveData: LiveData<EditProfilePageViewState> = _editProfilePageViewStateLiveData

    init {
        fetchData()
    }

    fun fetchData(){
        _editProfilePageViewStateLiveData.value = EditProfilePageViewState()
    }
}