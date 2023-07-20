package com.example.learnandroidproject.common.util

import android.content.Context
import android.provider.Settings
import java.util.*

class DeviceUuidFactory {
    companion object {
        private var uniqueID: String? = null
        private const val PREF_UNIQUE_ID: String = "PREF_UNIQUE_ID"

        @Synchronized
        fun create(context: Context): String {
            if (uniqueID == null) {
                val sharedPrefs = context.getSharedPreferences(PREF_UNIQUE_ID, Context.MODE_PRIVATE)
                uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null)
                if (uniqueID == null) {
                    uniqueID = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                    if (uniqueID.isNullOrEmpty()) {
                        uniqueID = UUID.randomUUID().toString()
                    }
                    val editor = sharedPrefs.edit()
                    with(editor) {
                        putString(PREF_UNIQUE_ID, uniqueID)
                        apply()
                    }
                }
            }
            return uniqueID as String
        }
    }
}