package com.lsper.client.activity.console

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.lsper.client.MyApplication
import com.lsper.client.R
import com.lsper.client.bean.Content
import io.github.xudaojie.qrcodelib.CaptureActivity
import kotlin.concurrent.thread


class BindActivity : AppCompatActivity() {
    //控制端发起绑定

    val REQUEST_QR_CODE = 1
    val TAG = "UUID"
    lateinit var clientUUIDEdit:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bind)
        var clientUUID: String?

        clientUUIDEdit = findViewById<EditText>(R.id.clientUUID)
        val bindBtn = findViewById<Button>(R.id.bind)

        bindBtn.setOnClickListener {
            //绑定UUID
            clientUUID = clientUUIDEdit.text.toString()
            clientUUID?.let { start(it) }
        }

        val QR = findViewById<Button>(R.id.QR)
        QR.setOnClickListener {
            val intent = Intent(this,CaptureActivity::class.java)
            startActivityForResult(intent, REQUEST_QR_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.e("onActivity","onActivity")
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode === RESULT_OK && requestCode === REQUEST_QR_CODE && data != null) {
            val result: String? = data?.getStringExtra("result")
            val arr = result?.split(":")
            if (arr?.get(0).equals("link-client")){
                if (result != null) {
                    arr?.get(1)?.let { start(it) }
                }
            }else{
                Toast.makeText(this,"二维码内容不正确",Toast.LENGTH_SHORT).show()
            }

        }
    }
    fun start(UUID:String){

        val sp = getSharedPreferences("UUID", MODE_PRIVATE)
        //将客户端的UUID写入本地
        val spe = sp.edit()
        spe.putString("BindUUID", UUID)
        spe.apply()

        thread {
            //开启线程连接server
            val gson = Gson()

            //发送连接信息
            val content = Content()
            content.senderUUID = sp.getString("localUUID",null)
            content.recipientUUID = UUID
            content.type = "connection"
            content.origin = "console"
            val json = gson.toJson(content)
            (applicationContext as MyApplication).getInstance().socketClient.send(json)
            val spe = getSharedPreferences("bind", MODE_PRIVATE).edit()
            spe.putBoolean("binded",true)
            spe.apply()
        }


        val intent = Intent(this, ConsoleActivity::class.java)
        startActivity(intent)
    }

}