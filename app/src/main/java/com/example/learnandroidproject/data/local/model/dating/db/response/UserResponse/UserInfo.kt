package com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse

import com.google.gson.annotations.SerializedName
data class UserInfo(
    @SerializedName("userId") var uId: Int,
    @SerializedName("username") var uName: String,
    @SerializedName("status") var uStatu: String,
    @SerializedName("url") var uPhoto: String,
    @SerializedName("lastMsg") var lastMessage: String?,
    @SerializedName("sendTime") var elapsedTime: String?,
    @SerializedName("isSeen") var seen: Boolean
) {
}