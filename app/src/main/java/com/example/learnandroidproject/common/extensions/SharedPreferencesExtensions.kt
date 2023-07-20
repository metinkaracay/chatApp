package com.example.learnandroidproject.common.extensions

import android.content.SharedPreferences
import com.example.learnandroidproject.common.util.StringUtils

fun SharedPreferences.getStringOrEmpty(key: String): String = this.getString(key, null) ?: StringUtils.EMPTY
fun SharedPreferences.getStringOrNull(key: String): String? = this.getString(key, null)