package com.lsper.client.console

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.gson.Gson
import com.lsper.client.R
import com.lsper.client.bean.Content
import com.lsper.client.bean.Sms
import com.lsper.client.socketClient
import com.lsper.client.websocket.SocketClient
import kotlin.concurrent.thread

class ConsoleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_console)
        val socket = socketClient


        val phoneNumber = findViewById<EditText>(R.id.phoneNumber)
        val time = findViewById<EditText>(R.id.lateTime)
        val smsContent = findViewById<EditText>(R.id.smsContent)
        val send = findViewById<Button>(R.id.send)

        send.setOnClickListener {
            val sms = Sms()
            if (time.text.toString()!="") {
                sms.lateTime = time.text.toString().toLong()
            }
            sms.phoneNumber = phoneNumber.text.toString()
            sms.smsContent = smsContent.text.toString()

            val sContent = Gson().toJson(sms)
            val sp = getSharedPreferences("UUID", MODE_PRIVATE)
            val localUUID = sp.getString("localUUID",null)
            val bindUUID = sp.getString("BindUUID",null)
            val content = Content(sContent,"sendSMS",localUUID,bindUUID,"console")
            val json = Gson().toJson(content)
            thread {
                socket.send(json)
            }

        }

    }
}