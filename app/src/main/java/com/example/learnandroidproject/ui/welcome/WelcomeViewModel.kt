package com.example.learnandroidproject.ui.welcome

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.SingleLiveEvent
import com.example.learnandroidproject.ui.common.navigation.NavigationData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor() : ViewModel() {

    private val _navigateToDestinationSingleLiveEvent: SingleLiveEvent<NavigationData> = SingleLiveEvent()
    private val _navigateUpSingleLiveEvent: SingleLiveEvent<Any?> = SingleLiveEvent()
    private val _closePageSingleLiveEvent: SingleLiveEvent<Any?> = SingleLiveEvent()

    val closePageSingleLiveEvent: LiveData<Any?> = _closePageSingleLiveEvent
    val navigateToDestinationSingleLiveEvent: LiveData<NavigationData> = _navigateToDestinationSingleLiveEvent
    val navigateUpSingleLiveEvent: LiveData<Any?> = _navigateUpSingleLiveEvent

    fun onSecondFragmentClicked() {
        _navigateToDestinationSingleLiveEvent.value = NavigationData(destinationId = R.id.secondFragment)
    }

    fun goToCreateProfile(userName:String, email:String, password:String){
        val bundle = Bundle()
        bundle.putString("userName", userName)
        bundle.putString("email", email)
        bundle.putString("password", password)
        _navigateToDestinationSingleLiveEvent.value = NavigationData(destinationId = R.id.createProfileFragment, args = bundle)
    }

    fun navigateUp() {
        _navigateUpSingleLiveEvent.call()
    }
}