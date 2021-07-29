package com.lsper.client

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.lsper.client.service.SocketBeatService
import com.lsper.client.websocket.SocketClient
import java.net.URI


class MyApplication: Application() {

    lateinit var socketBeatService: SocketBeatService.Bear
    lateinit var socketClient: SocketClient

    private val connection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            socketBeatService = service as SocketBeatService.Bear
            socketBeatService.startBeat()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            TODO("Not yet implemented")
        }

    }

    private lateinit var instance: MyApplication
    fun getInstance(): MyApplication {
        return instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }


    fun clientConnection(host:String,port:String) {
        try {
            socketClient = SocketClient(
                URI("ws://$host:$port"), this
            )
        } catch (e: Exception) {
            Log.e("socket", e.toString())
        }
        socketClient.connect()
        var i: Int = 0


        while (!socketClient.isOpen) {

            Log.e("num", i.toString())

            if (i != 1 && i % 10 == 1) {
                Log.e("num", i.toString())
            }
            Thread.sleep(1000)
            i++
        }
        //建立心跳
        val intent = Intent(this,SocketBeatService::class.java)
        bindService(intent,connection,Context.BIND_AUTO_CREATE)

    }
}