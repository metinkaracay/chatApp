package com.example.learnandroidproject.data.local.model.dating.db.request.chatApp

import com.google.gson.annotations.SerializedName

data class RaceData(
    @SerializedName("groupId") var groupId: Int,
    @SerializedName("userId") var userId: Int,
    @SerializedName("itemCount") var point: Int
) {
}