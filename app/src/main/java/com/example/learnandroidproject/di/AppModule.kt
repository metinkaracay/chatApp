package com.example.learnandroidproject.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.learnandroidproject.common.util.LocaleUtil
import com.example.learnandroidproject.data.remote.api.gson.BooleanTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(booleanTypeAdapter: BooleanTypeAdapter): Gson =
        GsonBuilder()
            .registerTypeAdapter(Boolean::class.java, booleanTypeAdapter)
            .setLenient()
            .create()

    @Provides
    @Singleton
    fun provideBooleanTypeAdapter() = BooleanTypeAdapter()

}