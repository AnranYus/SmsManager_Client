package com.lsper.client.console

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.gson.Gson
import com.lsper.client.R
import com.lsper.client.bean.Content
import com.lsper.client.socketClient
import com.lsper.client.websocket.SocketClient
import kotlin.concurrent.thread

class BindActivity : AppCompatActivity() {
    val TAG = "UUID"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bind)
        val socket = socketClient as SocketClient


        val bindBtn = findViewById<Button>(R.id.bind)
        val sp = getSharedPreferences("UUID", MODE_PRIVATE)

        var clientUUID: String?
        bindBtn.setOnClickListener {
            //绑定UUID
            val clientUUIDEdit = findViewById<EditText>(R.id.clientUUID)
            clientUUID = clientUUIDEdit.text.toString()
            if (clientUUID != null) {
                //将客户端的UUID写入本地
                val spe = sp.edit()
                spe.putString("BindUUID", clientUUID)
                spe.apply()

                thread {
                    //开启线程连接server
                    val gson = Gson()

                    //发送连接信息
                    val content = Content()
                    content.senderUUID = sp.getString("localUUID",null)
                    content.recipientUUID = clientUUID
                    content.type = "connection"
                    content.origin = "console"
                    val json = gson.toJson(content)
                    socket.send(json)

                }


                val intent = Intent(this, ConsoleActivity::class.java)
                startActivity(intent)

            }
        }



    }
}