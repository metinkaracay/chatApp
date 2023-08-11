package com.example.learnandroidproject.ui.welcome.groupChatsFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.ui.base.BaseViewModel
import com.github.michaelbull.result.get
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupChatsViewModel@Inject constructor(private val datingApiRepository: DatingApiRepository) : BaseViewModel() {

    private val _groupChatsPageViewStateLiveData: MutableLiveData<GroupChatsPageViewState> = MutableLiveData()
    val groupChatsPageViewStateLiveData: LiveData<GroupChatsPageViewState> = _groupChatsPageViewStateLiveData

    init {
        getAllGroups()
    }

    fun getAllGroups(){
        viewModelScope.launch(Dispatchers.IO){
            datingApiRepository.fetchGroups().get()?.let {
                viewModelScope.launch(Dispatchers.Main){
                    _groupChatsPageViewStateLiveData.value = GroupChatsPageViewState(it)
                }
            }
        }
    }
}