package com.example.learnandroidproject.ui.welcome.popUpFragment

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.learnandroidproject.ui.base.BaseViewModel

class PopUpViewModel: BaseViewModel() {

    private val _popUpPageViewStateLiveData: MutableLiveData<PopUpPageViewState> = MutableLiveData()
    val popupPageViewStateLiveData: LiveData<PopUpPageViewState> = _popUpPageViewStateLiveData

    private val _popUpCountDownTimer: MutableLiveData<Int> = MutableLiveData()
    val popUpCountDownTimer: LiveData<Int> = _popUpCountDownTimer

    init {
        _popUpPageViewStateLiveData.value = PopUpPageViewState()
        val timer: Long = 4
        val countDownInterval: Long = 1000

        object : CountDownTimer(timer * 1000, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = (millisUntilFinished / 1000).toInt()
                _popUpCountDownTimer.value = secondsRemaining
            }

            override fun onFinish() {
                _popUpCountDownTimer.value = 0
            }
        }.start()
    }
}