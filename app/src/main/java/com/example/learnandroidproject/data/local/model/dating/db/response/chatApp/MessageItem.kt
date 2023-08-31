package com.example.learnandroidproject.data.local.model.dating.db.response.chatApp

import com.google.gson.annotations.SerializedName

data class MessageItem(
    @SerializedName("message") val message: String,
    @SerializedName("type") val messageType: String,
    @SerializedName("senderId") val senderUser: String,
    @SerializedName("receiverId") val receiverUser: String,
    @SerializedName("sendTime") var messageTime: String
) {
}