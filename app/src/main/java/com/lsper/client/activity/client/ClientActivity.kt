package com.lsper.client.activity.client

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.lsper.client.MyApplication
import com.lsper.client.QRCodeUtil
import com.lsper.client.R
import com.lsper.client.bean.Content
import com.lsper.client.observer.SmsObserver
import com.lsper.client.service.GetSMSService
import com.lsper.client.websocket.SocketClient
import kotlin.concurrent.thread

class ClientActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)

        val intent = Intent(this,GetSMSService::class.java)
        startService(intent)
        //使用UUID作为唯一标识符
        val sp = getSharedPreferences("UUID", MODE_PRIVATE)
        val uuid = sp.getString("localUUID",null)

        val text = findViewById<TextView>(R.id.uuid)
        text.text = uuid

        val QRCode = findViewById<ImageView>(R.id.QRCode)
        val mBitmap = QRCodeUtil.createQRCodeBitmap("link-client:$uuid",880,880)
        QRCode.setImageBitmap(mBitmap)

        //生成Json
        val content = Content()
        content.senderUUID = uuid
        content.recipientUUID = null
        content.content = null
        content.origin = "client"
        content.type = "connection"

        val json = Gson().toJson(content)

        //运行客户端
        thread {
            (applicationContext as MyApplication).getInstance().socketClient.send(json)
        }


    }



}