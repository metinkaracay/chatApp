package com.example.learnandroidproject.ui.welcome.mainFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.learnandroidproject.ui.base.BaseViewModel

class MainFragmentViewModel: BaseViewModel() {

    private val _mainFragmentPageViewStateLiveData: MutableLiveData<MainFragmentPageViewState> = MutableLiveData()
    val mainFragmentPageViewStateLiveData: LiveData<MainFragmentPageViewState> = _mainFragmentPageViewStateLiveData

    init {
        fetchData()
    }

    fun fetchData(){
        _mainFragmentPageViewStateLiveData.value = MainFragmentPageViewState()
    }
}