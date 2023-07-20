package com.example.learnandroidproject.ui.welcome.testFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import com.example.learnandroidproject.ui.welcome.postTestFragment.PostViewModel
import com.github.michaelbull.result.get
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(private val datingApiRepository: DatingApiRepository): BaseViewModel() {

    private val _testPageViewStateLiveData: MutableLiveData<TestPageViewState> = MutableLiveData()

    val testPageViewStateLiveData: LiveData<TestPageViewState> = _testPageViewStateLiveData

    init {
        fetchData()
    }

    private fun fetchData(){
        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.payLoad().get()?.let {
                withContext(Dispatchers.Main){
                    _testPageViewStateLiveData.value = TestPageViewState(payloadBaseResponse = it)
                }
                Log.e("userName", "${it.payload.iat}")
            }
        }
    }
}