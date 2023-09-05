package com.example.learnandroidproject.data.local.model.dating.db.response.chatApp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
//@Entity(tableName = "messages", foreignKeys = [ForeignKey(entity = GroupInfo::class, parentColumns = ["id"], childColumns = ["groupId"], onDelete = ForeignKey.CASCADE)])
@Entity(tableName = "messages")
data class MessageItem(
    @ColumnInfo(name = "message")
    @SerializedName("message") var message: String,
    @ColumnInfo(name = "messageType")
    @SerializedName("type") val messageType: String,
    @ColumnInfo(name = "senderId")
    @SerializedName("senderId") val senderUser: String,
    @ColumnInfo(name = "receiverId")
    @SerializedName("receiverId") val receiverUser: String,
    @ColumnInfo(name = "sendTime")
    @SerializedName("sendTime") var messageTime: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}