package com.example.learnandroidproject.data.remote.model.dating.error

class DatingApiException(message: String? = null, private val errorResponse: DatingErrorResponse? = null) : Throwable(message)