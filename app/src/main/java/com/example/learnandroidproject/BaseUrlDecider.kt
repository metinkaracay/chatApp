package com.example.learnandroidproject

import com.example.learnandroidproject.common.util.Constants

object BaseUrlDecider {

    fun getApiBaseUrl() = if (BuildConfig.DEBUG) {
        Constants.STAGE_BASE_URL
    } else {
        Constants.STAGE_BASE_URL
    }

    fun getSocketBaseUrl() = if (BuildConfig.DEBUG) {
        Constants.STAGE_BASE_URL
    } else {
        Constants.STAGE_BASE_URL
    }
}