package com.example.learnandroidproject.ui.splash

import android.os.Bundle
import androidx.activity.viewModels
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.*
import com.example.learnandroidproject.databinding.ActivitySplashBinding
import com.example.learnandroidproject.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    private val splashViewModel: SplashViewModel by viewModels()

    override fun getLayoutResId(): Int = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        with(splashViewModel) {
            navigateToWelcomeSingleLiveEvent.observe(this@SplashActivity) {
                startActivity(buildWelcomeIntent())
                finish()
            }

            init()
        }
    }
}