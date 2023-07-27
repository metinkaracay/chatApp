package com.example.learnandroidproject.ui.welcome.baseChatRooms

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import com.github.michaelbull.result.get
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BaseChatRoomsViewModel @Inject constructor(private val datingApiRepository: DatingApiRepository): BaseViewModel() {

    private val _baseChatRoomsPageViewStateLiveData: MutableLiveData<BaseChatRoomsPageViewState> = MutableLiveData()
    val baseChatRoomsPageViewStateLiveData: LiveData<BaseChatRoomsPageViewState> = _baseChatRoomsPageViewStateLiveData

    fun getLoggedUser(){
        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.fetchUserData().get()?.let {
                withContext(Dispatchers.Main){
                    _baseChatRoomsPageViewStateLiveData.value = BaseChatRoomsPageViewState(it.photo!!)
                }
            }
        }
    }
}