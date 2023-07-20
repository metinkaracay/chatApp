package com.example.learnandroidproject.di

import com.example.learnandroidproject.data.remote.api.dating.DatingApiService
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepository
import com.example.learnandroidproject.domain.remote.dating.DatingApiRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RemoteRepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideDatingApiRepository(datingApiService: DatingApiService): DatingApiRepository {
        return DatingApiRepositoryImpl(datingApiService = datingApiService)
    }
}