package com.example.learnandroidproject.ui.welcome.groupChatsFragment

import com.example.learnandroidproject.ui.welcome.groupChatsFragment.adapter.GroupsAdapter
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
    fun provideSGroupAdapter() = GroupsAdapter()
}