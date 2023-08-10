package com.example.learnandroidproject.ui.welcome

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.learnandroidproject.R
import com.example.learnandroidproject.common.extensions.observeNonNull
import com.example.learnandroidproject.common.tryOrLog
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.AdditionalData
import com.example.learnandroidproject.databinding.ActivityWelcomeBinding
import com.example.learnandroidproject.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
class WelcomeActivity : BaseActivity<ActivityWelcomeBinding>() {

    private val navController by lazy(LazyThreadSafetyMode.NONE) {
        (supportFragmentManager.findFragmentById(R.id.navHostFragmentWelcome) as NavHostFragment).navController
    }

    private val welcomeViewModel: WelcomeViewModel by viewModels()
    private var appState = true

    override fun getLayoutResId(): Int = R.layout.activity_welcome

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavigationComponentAndBottomNavigationView()
        EventBus.getDefault().register(this)
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
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onBackground() {
                appState = false
                Log.e("uygulama durumu","arka planda")
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onForeground() {
                appState = true
                Log.e("uygulama durumu","Ön planda")
            }
        })
    }

    private fun setupNavigationComponentAndBottomNavigationView() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragmentWelcome) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.welcome_nav_graph)
        graph.setStartDestination(R.id.mainFragment)
        navController.setGraph(graph, intent.extras)

        navController.addOnDestinationChangedListener { _, destination, _ -> welcomeViewModel.onDestinationChanged(destinationId = destination.id) }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onAdditionalData(event: AdditionalData){
        val roomId = event.senderId
        welcomeViewModel.fillAdditionalId(roomId)
    }
    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}