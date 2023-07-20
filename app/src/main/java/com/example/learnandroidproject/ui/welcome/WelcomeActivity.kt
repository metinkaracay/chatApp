package com.example.learnandroidproject.ui.welcome

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.common.tryOrLog
import com.example.learnandroidproject.databinding.ActivityWelcomeBinding
import com.example.learnandroidproject.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeActivity : BaseActivity<ActivityWelcomeBinding>() {

    private val navController by lazy(LazyThreadSafetyMode.NONE) {
        (supportFragmentManager.findFragmentById(R.id.navHostFragmentWelcome) as NavHostFragment).navController
    }

    private val welcomeViewModel: WelcomeViewModel by viewModels()

    override fun getLayoutResId(): Int = R.layout.activity_welcome

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(welcomeViewModel) {
            navigateToDestinationSingleLiveEvent.observeNonNull(this@WelcomeActivity) {
                navigateToDestination(navController, it)
            }

            navigateUpSingleLiveEvent.observe(this@WelcomeActivity) {
                tryOrLog { navController.navigateUp() }
            }

            closePageSingleLiveEvent.observe(this@WelcomeActivity) {
                finish()
            }
        }

    }
}