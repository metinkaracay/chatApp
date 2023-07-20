package com.example.learnandroidproject.common.extensions

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

fun Context.isLocationPermissionGranted(): Boolean {
    if (Build.VERSION.SDK_INT < 23) {
        return true
    }
    val granted = this.checkCallingOrSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    return granted == PackageManager.PERMISSION_GRANTED
}

fun Context.isStoragePermissionGranted(): Boolean {
    if (Build.VERSION.SDK_INT < 23) {
        return true
    }
    val readExternalStorageGranted = this.checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    val writeExternalStorageGranted = this.checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    return readExternalStorageGranted == PackageManager.PERMISSION_GRANTED && writeExternalStorageGranted == PackageManager.PERMISSION_GRANTED
}

fun Context.isRecordAudioPermissionGranted(): Boolean {
    if (Build.VERSION.SDK_INT < 23) {
        return true
    }
    val granted = this.checkCallingOrSelfPermission(android.Manifest.permission.RECORD_AUDIO)
    return granted == PackageManager.PERMISSION_GRANTED
}

fun Context.isCameraPermissionGranted(): Boolean {
    if (Build.VERSION.SDK_INT < 23) {
        return true
    }
    val granted = this.checkCallingOrSelfPermission(android.Manifest.permission.CAMERA)
    return granted == PackageManager.PERMISSION_GRANTED
}