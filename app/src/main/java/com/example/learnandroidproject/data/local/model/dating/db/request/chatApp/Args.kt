package com.example.learnandroidproject.data.local.model.dating.db.request.chatApp

data class Args(
    var message: String,
    var senderId: String,
    var receiverId: String,
    var messageTime: String,
    var messageType: String,
    var seen: Boolean
) {
}