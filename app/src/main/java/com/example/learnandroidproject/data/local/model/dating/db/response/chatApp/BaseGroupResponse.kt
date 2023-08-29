package com.example.learnandroidproject.data.local.model.dating.db.response.chatApp

import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.RaceData
import com.google.gson.annotations.SerializedName


data class BaseGroupResponse(
    @SerializedName("messages") val messageList: List<MessageItem>,
    @SerializedName("users") val userList: List<GroupMember>,
    @SerializedName("race") val raceState: List<RaceData>,
    @SerializedName("timeLeft") val remainingTime: Int,
    @SerializedName("userItemCount") val ghostItemCount: Int
) {
}