package com.example.learnandroidproject.ui.welcome.createGroupFragment

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
class CreateGroupViewModel @Inject constructor(private val datingApiRepository: DatingApiRepository) : BaseViewModel() {

    private val _createGroupPageViewStateLiveData: MutableLiveData<CreateGroupPageViewState> = MutableLiveData()
    val createGroupPageViewStateLiveData: LiveData<CreateGroupPageViewState> = _createGroupPageViewStateLiveData

    private val _setNextButtonVisibilityStateLiveData: MutableLiveData<CreateGroupPageViewState> = MutableLiveData()
    val setNextButtonVisibilityStateLiveData: LiveData<CreateGroupPageViewState> = _setNextButtonVisibilityStateLiveData

    init {
        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.fetchAllUsers().get()?.let {
                withContext(Dispatchers.Main){
                    _createGroupPageViewStateLiveData.value = CreateGroupPageViewState(it,false)
                }
            }
        }
    }

    /*fun setNextButtonState(state: Boolean){ TODO düzelt burayı
        Log.e("gelen state","$state")
        _setNextButtonVisibilityStateLiveData.value = setNextButtonVisibilityStateLiveData.value?.copy(isUserSelect = state)
    }*/
}