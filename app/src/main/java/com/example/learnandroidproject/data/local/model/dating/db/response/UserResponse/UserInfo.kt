package com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse

import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("id") val uId: Int,
    @SerializedName("username") val uName: String,
    @SerializedName("status") val uStatu: String,
    @SerializedName("url") val uPhoto: String
) {
}