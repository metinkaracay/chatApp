package com.example.learnandroidproject.ui.base

import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.learnandroidproject.App
import io.reactivex.rxjava3.disposables.CompositeDisposable

open class BaseAndroidViewModel(context: Context) : AndroidViewModel(context as App) {

    open val compositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}