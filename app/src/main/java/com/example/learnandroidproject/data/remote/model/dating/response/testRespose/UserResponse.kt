package com.example.learnandroidproject.data.remote.model.dating.response.testRespose

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id") val uId: Int,
    @SerializedName("email") val email: String,
    @SerializedName("name") val uName: String?,
    @SerializedName("created_at") val create: String,
    @SerializedName("updated_at") val upload: String
) {
}