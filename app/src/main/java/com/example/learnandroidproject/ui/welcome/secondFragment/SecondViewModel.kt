package com.example.learnandroidproject.ui.welcome.secondFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.learnandroidproject.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SecondViewModel @Inject constructor() : BaseViewModel() {

    private val _secondFragmentPageViewStateLiveData: MutableLiveData<SecondFragmentPageViewState> = MutableLiveData()

    val secondFragmentPageViewStateLiveData: LiveData<SecondFragmentPageViewState> = _secondFragmentPageViewStateLiveData

    init {
        _secondFragmentPageViewStateLiveData.value = SecondFragmentPageViewState()
    }

}