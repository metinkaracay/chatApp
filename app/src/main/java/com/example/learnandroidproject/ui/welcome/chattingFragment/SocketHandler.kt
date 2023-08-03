package com.example.learnandroidproject.ui.welcome.chattingFragment

import android.content.Context
import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object SocketHandler {

    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket(context: Context){
        val sharedPreferences = context.getSharedPreferences("accessTokenShared", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("accessTokenKey", "")
        try {
            mSocket = IO.socket("http://10.82.0.73:3000/api/v1/chat?token=${token}")
        } catch (e: URISyntaxException){
            Log.e("Socket Hatası","$e")
        }
    }

    @Synchronized
    fun getSocket(): Socket{
        return mSocket
    }

    @Synchronized
    fun establishConnection(){
        mSocket.connect()
    }

    @Synchronized
    fun closeConnection(){
        mSocket.disconnect()
    }
}