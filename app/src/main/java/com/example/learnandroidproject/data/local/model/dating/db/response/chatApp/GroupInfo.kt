package com.example.learnandroidproject.data.local.model.dating.db.response.chatApp

import com.google.gson.annotations.SerializedName

data class GroupInfo(
    @SerializedName("id") var groupId: Int,
    @SerializedName("groupName") var groupName: String,
    @SerializedName("url") var groupPhoto: String,
    @SerializedName("sendTime") var messageTime: String,
    @SerializedName("lastMsg") var lastMessage: String,
    @SerializedName("isSeen") var isSeen: Boolean?
) {
}