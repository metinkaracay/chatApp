package com.example.learnandroidproject.data.remote.api.dating

import android.util.Log
import com.example.learnandroidproject.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*
import javax.inject.Inject

class DatingApiHeaderInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        val modifiedRequest = requestBuilder
            .addHeader(HEADER_COUNTRY, Locale.getDefault().country)
            .addHeader(HEADER_LANGUAGE, Locale.getDefault().language)
            .addHeader(HEADER_PLATFORM, "1")
            .addHeader(X_REQ_LANG_CODE, Locale.getDefault().language)
            .addHeader(HEADER_VERSION, BuildConfig.VERSION_CODE.toString())
            .build()
        return chain.proceed(modifiedRequest)
    }

    companion object {
        private const val HEADER_DATING_TOKEN = "Authorization"
        private const val HEADER_COUNTRY = "country"
        private const val HEADER_LANGUAGE = "application-country"
        private const val HEADER_PLATFORM = "x-req-platform"
        private const val X_REQ_LANG_CODE = "x-req-langCode"
        private const val HEADER_VERSION = "x-req-version"
    }
}