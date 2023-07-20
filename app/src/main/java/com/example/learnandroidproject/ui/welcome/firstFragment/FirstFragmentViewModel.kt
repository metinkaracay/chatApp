package com.example.learnandroidproject.ui.welcome.firstFragment

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
class FirstFragmentViewModel @Inject constructor(
    private val datingApiRepository: DatingApiRepository
) : BaseViewModel() {

    private val _firstFragmentPageViewStateLiveData: MutableLiveData<FirstFragmentPageViewState> = MutableLiveData()

    val firstFragmentPageViewStateLiveData: LiveData<FirstFragmentPageViewState> = _firstFragmentPageViewStateLiveData

    init {
        _firstFragmentPageViewStateLiveData.value = FirstFragmentPageViewState()

        fetchNews()
    }

    private fun fetchNews() {
        viewModelScope.launch(Dispatchers.IO) {

            datingApiRepository.fetchNews().get().let {
                Log.e("base_response", "${it?.articles}")
            }
        }
    }
}