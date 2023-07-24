package com.example.learnandroidproject.ui.welcome.generalChatUsersFragment

import com.example.learnandroidproject.ui.welcome.generalChatUsersFragment.adapter.GeneralUsersAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
object SubscriptionFragmentModule {
    @Provides
    @FragmentScoped
    fun provideSGeneralChatUsersAdapter() = GeneralUsersAdapter()
}