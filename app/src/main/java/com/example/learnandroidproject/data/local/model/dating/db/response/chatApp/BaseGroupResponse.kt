package com.example.learnandroidproject.data.local.model.dating.db.response.chatApp

import com.google.gson.annotations.SerializedName


data class BaseGroupResponse(
    @SerializedName("messages") val messageList: List<MessageItem>,
    @SerializedName("users") val userList: List<GroupMember>
) {
}