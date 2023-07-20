package com.example.learnandroidproject.common.extensions

import com.example.learnandroidproject.common.util.StringUtils
import com.example.learnandroidproject.data.remote.api.StatusCode
import com.example.learnandroidproject.data.remote.model.dating.error.DatingApiException
import com.example.learnandroidproject.data.remote.model.dating.error.DatingErrorResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.HttpException
import java.lang.Exception

fun Throwable?.isTooManyRequestException(): Boolean {
    this?.let {
        if (it is HttpException) {
            val code = it.code()
            if (code == StatusCode.TooManyRequests.code) {
                return true
            }
        }
    }
    return false
}

fun Throwable.asDatingApiException(): DatingApiException {
    return if (this is HttpException) {
        try {
            val gson = Gson()
            val type = object : TypeToken<DatingErrorResponse>() {}.type
            val errorResponse: DatingErrorResponse = gson.fromJson(response()?.errorBody()?.charStream(), type)
            DatingApiException(message = errorResponse.message, errorResponse = errorResponse)
        } catch (e: Exception) {
            val errorResponse = DatingErrorResponse(statusCode = this.code(), message = StringUtils.EMPTY)
            DatingApiException(errorResponse = errorResponse)
        }
    } else {
        DatingApiException()
    }
}