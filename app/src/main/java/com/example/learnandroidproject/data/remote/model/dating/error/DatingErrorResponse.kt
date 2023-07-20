package com.example.learnandroidproject.data.remote.model.dating.error

import com.google.gson.annotations.SerializedName

data class DatingErrorResponse(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("message") val message: String?
)
