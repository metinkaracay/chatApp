package com.example.learnandroidproject

import android.app.Application
import android.content.Context
import androidx.lifecycle.LifecycleObserver
import androidx.multidex.MultiDex
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

@HiltAndroidApp
class App : Application(), CoroutineScope, LifecycleObserver {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun onCreate() {
        super.onCreate()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        MultiDex.install(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        job.cancel()
    }
}