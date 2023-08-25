package com.example.learnandroidproject.ui.splash

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnandroidproject.common.SingleLiveEvent
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.github.michaelbull.result.get
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
) : ViewModel() {

    private val _navigateToWelcomeSingleLiveEvent: SingleLiveEvent<Any?> = SingleLiveEvent()

    val navigateToWelcomeSingleLiveEvent: LiveData<Any?> = _navigateToWelcomeSingleLiveEvent

    fun init() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { _navigateToWelcomeSingleLiveEvent.call() }
        }

    }

    fun downloadVideo(url: String,fileName: String,context: Context){
        Log.e("viremodellog","$url")
        Log.e("viremodellog","name : $fileName")
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Downloading Video")
            .setDescription("Please wait while the video is being downloaded...")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = downloadManager.enqueue(request)
    }
}