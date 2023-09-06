package com.example.learnandroidproject.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem

@Dao
interface MessageDao {

    @Insert
    suspend fun insertAllMessages(messages: List<MessageItem>)

    @Insert
    suspend fun insertMessage(messages: MessageItem)

    @Query("SELECT * FROM messages WHERE (senderId = :loggedId AND receiverId = :receiverId) OR (senderId = :receiverId AND receiverId = :loggedId) ORDER BY messages.sendTime DESC LIMIT 10 OFFSET :skipped")//
    suspend fun getAllMessages(loggedId: Int, receiverId: Int, skipped: Int): List<MessageItem>

    @Query("SELECT * FROM messages WHERE messages.sendTime = (SELECT MAX(sendTime) FROM messages) ")
    suspend fun getLastMessage(): MessageItem

    @Query("SELECT * FROM messages WHERE (messages.senderId = :receiverId OR messages.receiverId = :receiverId) AND messages.sendTime = (SELECT MAX(sendTime) FROM messages) ")
    suspend fun filterMessages(receiverId : String) : MessageItem
}