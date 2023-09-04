package com.example.learnandroidproject.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.learnandroidproject.data.local.dao.MessageDao
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem

@Database(entities = [MessageItem::class], version = 13)
abstract class ChatDatabase : RoomDatabase() {

    abstract fun MessageDao() : MessageDao

    companion object {

        @Volatile // Diğer Thread'lere de görünür yapar
        private var instance: ChatDatabase? = null

        fun getInstance(context: Context): ChatDatabase {
            return instance ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context.applicationContext,
                    ChatDatabase::class.java,
                    "chat_database").fallbackToDestructiveMigration().build()
                instance = database
                database
            }
        }
    }
}