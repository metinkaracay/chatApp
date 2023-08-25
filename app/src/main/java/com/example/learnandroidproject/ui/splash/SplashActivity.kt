package com.example.learnandroidproject.ui.splash

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.viewModels
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.*
import com.example.learnandroidproject.databinding.ActivitySplashBinding
import com.example.learnandroidproject.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URL

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    private val splashViewModel: SplashViewModel by viewModels()

    override fun getLayoutResId(): Int = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val blackCarUrl = "https://chat-appbucket.s3.eu-central-1.amazonaws.com/Asset+38%402x+2-luma.mp4"
        val yellowCarUrl = "https://chat-appbucket.s3.eu-central-1.amazonaws.com/Asset+42%402x+2-luma.mp4"
        val redCarUrl = "https://chat-appbucket.s3.eu-central-1.amazonaws.com/Asset+41%402x+2-luma.mp4"
        val roadUrl = "https://chat-appbucket.s3.eu-central-1.amazonaws.com/Group+5222.mp4"
        setContentView(R.layout.activity_splash)
        with(splashViewModel) {
            navigateToWelcomeSingleLiveEvent.observe(this@SplashActivity) {
                startActivity(buildWelcomeIntent())
                finish()
            }

            init()
            /*val fileName = "road_video3"
            val request = DownloadManager.Request(Uri.parse(fileUrl3))
                .setTitle("Downloading Video")
                .setDescription("Please wait while the video is being downloaded...")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadId = downloadManager.enqueue(request)*/


            // Dosya adlarını saklamak için bir Set oluşturun
            val downloadedFiles = mutableSetOf<String>()

            // Önceden indirilen dosyaları SharedPreferences'ta kontrol edin
            val sharedPrefs = getSharedPreferences("downloaded_videos", Context.MODE_PRIVATE)
            val downloadedFilesString = sharedPrefs.getStringSet("downloaded_videos_set", setOf())
            downloadedFiles.addAll(downloadedFilesString ?: emptySet())

            if (!downloadedFiles.contains(blackCarUrl)) {
                splashViewModel.downloadVideo(blackCarUrl,"black_car_video",applicationContext)
                downloadedFiles.add(blackCarUrl)
            }

            if (!downloadedFiles.contains(yellowCarUrl)) {
                splashViewModel.downloadVideo(yellowCarUrl,"yellow_car_video",applicationContext)
                downloadedFiles.add(yellowCarUrl)
            }
            if (!downloadedFiles.contains(redCarUrl)) {
                splashViewModel.downloadVideo(redCarUrl,"red_car_video",applicationContext)
                downloadedFiles.add(redCarUrl)
            }
            if (!downloadedFiles.contains(roadUrl)) {
                Log.e("test_shared","buraya girdi")
                splashViewModel.downloadVideo(roadUrl,"road_video",applicationContext)
                downloadedFiles.add(roadUrl)
            }

            sharedPrefs.edit().putStringSet("downloaded_videos_set", downloadedFiles).apply()

            /*CoroutineScope(Dispatchers.IO).launch {
                val connection = URL(fileUrl).openConnection()
                val inputStream = BufferedInputStream(connection.getInputStream())
                val outputFile = File(Environment.getExternalStorageDirectory(), "new_car")
                val outputStream = FileOutputStream(outputFile)

                val data = ByteArray(1024)
                var count: Int
                while (inputStream.read(data).also { count = it } != -1) {
                    outputStream.write(data, 0, count)
                }

                outputStream.flush()
                outputStream.close()
                inputStream.close()
            }*/
        }
    }
}