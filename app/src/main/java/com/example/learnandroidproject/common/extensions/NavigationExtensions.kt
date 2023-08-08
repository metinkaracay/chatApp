package com.example.learnandroidproject.common.extensions

import android.content.Context
import android.content.Intent
import com.example.learnandroidproject.ui.splash.SplashActivity
import com.example.learnandroidproject.ui.welcome.WelcomeActivity

fun Context.buildWelcomeIntent() = Intent(this, WelcomeActivity::class.java)
fun Context.buildSplashIntent() = Intent(this, SplashActivity::class.java)

