package com.example.learnandroidproject.ui.welcome.chattingFragment

import android.content.Context
import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import java.net.URISyntaxException

object SocketHandler {

    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket(context: Context){
        val sharedPreferences = context.getSharedPreferences("accessTokenShared", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("accessTokenKey", "")
        try {
            val opts = IO.Options()
            var name = arrayOf("websocket")
            opts.transports = name
            //mSocket = IO.socket("http://10.82.0.57:3000/?token=${token}", opts)
            //mSocket = IO.socket("http://ec2-3-66-189-165.eu-central-1.compute.amazonaws.com:3000/?token=${token}", opts)
            mSocket = IO.socket("http://chat-app-env.eba-ev2ugfcu.eu-central-1.elasticbeanstalk.com/?token=${token}", opts)
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
        Log.e("setSocket","Bağlantı kuruldu")
    }

    @Synchronized
    fun closeConnection(){
        mSocket.disconnect()
    }
}