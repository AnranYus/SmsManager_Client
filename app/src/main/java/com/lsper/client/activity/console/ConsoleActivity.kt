package com.lsper.client.activity.console

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.gson.Gson
import com.lsper.client.MyApplication
import com.lsper.client.R
import com.lsper.client.bean.Content
import com.lsper.client.bean.Sms
import kotlin.concurrent.thread

class ConsoleActivity : AppCompatActivity() {
    var localUUID: String? = null
    var bindUUID:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_console)
        val sp = getSharedPreferences("UUID", MODE_PRIVATE)
        localUUID = sp.getString("localUUID",null)
        bindUUID = sp.getString("BindUUID",null)

        val phoneNumber = findViewById<EditText>(R.id.phoneNumber)
        val time = findViewById<EditText>(R.id.lateTime)
        val smsContent = findViewById<EditText>(R.id.smsContent)
        val send = findViewById<Button>(R.id.send)

        val bindedsp = getSharedPreferences("bind", MODE_PRIVATE);
        //连接通信
        if (bindedsp.getBoolean("binded",false)){
            connInformation()
        }

        send.setOnClickListener {
            val sms = Sms()
            if (time.text.toString()!="") {
                sms.lateTime = time.text.toString().toLong()
            }
            sms.phoneNumber = phoneNumber.text.toString()
            sms.smsContent = smsContent.text.toString()

            val sContent = Gson().toJson(sms)
            val content = Content(sContent,"sendSMS",localUUID,bindUUID,"console")
            val json = Gson().toJson(content)
            thread {
                (application as MyApplication).getInstance().socketClient.send(json)
            }

        }

    }
    private fun connInformation(){
        //读取绑定的UUID
        val content = Content()

        //发送连接信息
        content.senderUUID = localUUID
        content.recipientUUID = bindUUID
        content.type = "connection"
        content.origin = "console"
        val json = Gson().toJson(content)
        (application as MyApplication).getInstance().socketClient.send(json)
    }
}