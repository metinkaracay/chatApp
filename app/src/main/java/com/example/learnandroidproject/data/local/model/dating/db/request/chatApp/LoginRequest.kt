package com.example.learnandroidproject.data.local.model.dating.db.request.chatApp

import com.google.gson.annotations.SerializedName

class LoginRequest(
    @SerializedName("username") val userName: String,
    @SerializedName("password") val password: String,
    @SerializedName("pushToken") val oneSignalKey: String?
) {
}