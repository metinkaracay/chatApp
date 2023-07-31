package com.example.learnandroidproject.ui.welcome.baseChatRooms

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.common.isSuccess
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
class BaseChatRoomsViewModel @Inject constructor(private val datingApiRepository: DatingApiRepository): BaseViewModel() {

    private val _baseChatRoomsPageViewStateLiveData: MutableLiveData<BaseChatRoomsPageViewState> = MutableLiveData()
    val baseChatRoomsPageViewStateLiveData: LiveData<BaseChatRoomsPageViewState> = _baseChatRoomsPageViewStateLiveData

    private val _exitResponseLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val exitResponseLiveData: LiveData<Boolean> = _exitResponseLiveData

    fun getLoggedUser(){
        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.fetchUserData().get()?.let {
                withContext(Dispatchers.Main){
                    _baseChatRoomsPageViewStateLiveData.value = BaseChatRoomsPageViewState(it.photo!!)
                }
            }
        }
    }
    fun exitToServer(context: Context){
        viewModelScope.launch(Dispatchers.IO){
            val result = datingApiRepository.exit()
            if (result.isSuccess()){
                val sharedPreferences = context.getSharedPreferences(context.packageName,Context.MODE_PRIVATE)
                sharedPreferences.edit().remove("accessTokenKey").apply()
                _exitResponseLiveData.postValue(true)
            }else{
                val error = result.component2()
                if (error != null && error is retrofit2.HttpException) {
                    if (error.code() == 401) {
                        _exitResponseLiveData.postValue(false)
                    } else {
                        _exitResponseLiveData.postValue(false)
                    }
                } else {
                    _exitResponseLiveData.postValue(false)
                }
                _exitResponseLiveData.postValue(false)
            }
        }
    }
}