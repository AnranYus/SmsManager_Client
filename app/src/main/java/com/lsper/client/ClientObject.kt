package com.lsper.client

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.lsper.client.websocket.SocketClient
import java.net.URI

lateinit var socketClient: SocketClient
lateinit var toast: Toast

fun clientConnection(host:String,port:String,context:Context): SocketClient {
    socketClient = SocketClient(
        URI("ws://$host:$port"),context
    )
    socketClient.connect()
    var i:Int = 0


    while (!socketClient.isOpen){

        Log.e("num",i.toString())

        if (i!=1&&i %10==1) {
            Log.e("num",i.toString())
            toast.setText("请检查IP地址、端口号或网络连接")
            toast.show()
        }
        Thread.sleep(1000)
        i++
    }

    return socketClient


}