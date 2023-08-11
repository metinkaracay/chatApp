package com.example.learnandroidproject.ui.welcome.createGroupFragment

import com.example.learnandroidproject.ui.welcome.createGroupFragment.adapter.CreateGroupUsersAdapter
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
    fun provideSCreateGroupUsersAdapter() = CreateGroupUsersAdapter()
}