package com.example.learnandroidproject.ui.welcome.chattingFragment

import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object SocketHandler {

    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket(context: Context){
        val sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("accessTokenKey", "")
        try {
            mSocket = IO.socket("http://10.82.0.135:3000/api/v1/chat?token=${token}")
            Log.e("socket","http://10.82.0.135:3000/api/v1/chat?token=${token}")
        } catch (e: URISyntaxException){
            Log.e("socket Hatası","$e")
        }
    }

    @Synchronized
    fun getSocket(): Socket{
        Log.e("socket","getsoket çalıştı")
        return mSocket
    }

}