package com.example.learnandroidproject.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.learnandroidproject.BaseUrlDecider
import com.example.learnandroidproject.BuildConfig
import com.example.learnandroidproject.data.remote.api.dating.DatingApi
import com.example.learnandroidproject.data.remote.api.dating.DatingApiAuthenticator
import com.example.learnandroidproject.data.remote.api.dating.DatingApiService
import com.example.learnandroidproject.data.remote.api.dating.DatingApiHeaderInterceptor
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /* Chuck Interceptor */
    @Singleton
    @Provides
    fun provideChuckInterceptor(@ApplicationContext context: Context) = ChuckerInterceptor.Builder(context).build()


    @Provides
    @Singleton
    fun provideDatingApiService(@DatingApi retrofit: Retrofit): DatingApiService = retrofit.create(DatingApiService::class.java)

    @DatingApi
    @Singleton
    @Provides
    fun provideDatingRetrofit(@DatingApi client: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl(BaseUrlDecider.getApiBaseUrl())
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @DatingApi
    @Singleton
    @Provides
    fun provideDatingKitOkHttpClient(
        chuckInterceptor: ChuckerInterceptor,
        datingApiHeaderInterceptor: DatingApiHeaderInterceptor,
        datingApiAuthenticator: DatingApiAuthenticator
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .readTimeout(if (BuildConfig.DEBUG) 30 else 60, TimeUnit.SECONDS)
            .writeTimeout(if (BuildConfig.DEBUG) 30 else 60, TimeUnit.SECONDS)
            .addInterceptor(chuckInterceptor)
            .addInterceptor(datingApiHeaderInterceptor)
            .authenticator(datingApiAuthenticator)
        return builder.build()
    }

    @Provides
    fun provideContext(
        @ApplicationContext context: Context,
    ): Context {
        return context
    }
}