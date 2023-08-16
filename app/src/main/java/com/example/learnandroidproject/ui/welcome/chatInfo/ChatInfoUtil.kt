package com.example.learnandroidproject.ui.welcome.chatInfo

import com.example.learnandroidproject.ui.welcome.chatInfo.adapter.MembersAdapter
import com.example.learnandroidproject.ui.welcome.chattingFragment.adapter.ChattingMessagesAdapter
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
    fun provideSMembersAdapter() = MembersAdapter()
}