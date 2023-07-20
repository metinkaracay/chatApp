package com.example.learnandroidproject.di

import androidx.fragment.app.FragmentActivity
import com.tbruyelle.rxpermissions3.RxPermissions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object RxPermissionsActivityModule {

    @Provides
    fun provideRxPermissions(fragmentActivity: FragmentActivity) = RxPermissions(fragmentActivity)
}