package com.example.learnandroidproject

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.multidex.MultiDex
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.AdditionalData
import com.onesignal.OSSubscriptionObserver
import com.onesignal.OSSubscriptionStateChanges
import com.onesignal.OneSignal
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

const val ONESIGNAL_APP_ID ="4434c576-bd32-404f-96a1-5410b56217dd"
@HiltAndroidApp
class App : Application(), CoroutineScope, LifecycleObserver {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun onCreate() {
        super.onCreate()

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)

        OneSignal.promptForPushNotifications()

        OneSignal.addSubscriptionObserver(object : OSSubscriptionObserver {
            override fun onOSSubscriptionChanged(stateChanges: OSSubscriptionStateChanges?) {
                if (stateChanges?.to?.isSubscribed == true) {
                    val oneSignalPlayerId = stateChanges.to.userId
                    Log.e("OneSignal Token", "$oneSignalPlayerId")
                }
            }
        })
        val oneSignalPlayerId = OneSignal.getDeviceState()?.userId

        OneSignal.setNotificationOpenedHandler { result ->
            Log.e("additionRoomIdresult","${result.notification.additionalData}")
            if (result.notification.additionalData != null) {
                resolveNotification(data = result.notification.additionalData)
                val roomId = result.notification.additionalData.optString("senderId")
                Log.e("additionRoomId","$roomId")
                EventBus.getDefault().postSticky(AdditionalData(roomId))
            }
        }

        Log.e("signalId","$oneSignalPlayerId")
    }

    fun resolveNotification(data: JSONObject){
        val roomId = data.optString("senderId")
        Log.e("additionResol","$roomId")
        EventBus.getDefault().post(AdditionalData(roomId))
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