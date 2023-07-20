package com.example.learnandroidproject.data.remote.model.dating.response.testRespose

import com.google.gson.annotations.SerializedName

class PayloadResponse(
    @SerializedName("user") val user: UserResponse,
    @SerializedName("iat") val iat: Int,
    @SerializedName("exp") val exp: Int
) {
}