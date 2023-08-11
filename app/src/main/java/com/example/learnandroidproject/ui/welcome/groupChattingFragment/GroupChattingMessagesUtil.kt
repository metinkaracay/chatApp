package com.example.learnandroidproject.ui.welcome.groupChattingFragment

import com.example.learnandroidproject.ui.welcome.chattingFragment.adapter.ChattingMessagesAdapter
import com.example.learnandroidproject.ui.welcome.groupChattingFragment.adapter.GroupChattingMessagesAdapter
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
    fun provideSGroupChattingMessagesAdapter() = GroupChattingMessagesAdapter()
}