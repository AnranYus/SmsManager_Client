package com.lsper.client

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.lsper.client.client.ClientActivity
import com.lsper.client.console.BindActivity
import com.lsper.client.console.ConsoleActivity
import java.util.*
import kotlin.concurrent.thread


class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        toast = Toast.makeText(this,"",Toast.LENGTH_SHORT)

        //检查权限
        val hasWriteStoragePermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.SEND_SMS
        )
        if (hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED) {
            //拥有权限，执行操作
            init()
        } else {
            //没有权限，向用户请求权限
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.SEND_SMS),
                1
            )
        }



    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    init()
                }else{
                    Toast.makeText(this,"你必须授予权限才能运行全部功能",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun init(){
        //读取或生成控制端UUID
        val spUUID = getSharedPreferences("UUID", MODE_PRIVATE)
        var uuid = spUUID.getString("localUUID",null)
        //如果没有 则生成一个UUID
        if (uuid ==null){
            val sp_edit = spUUID.edit()
            uuid = UUID.randomUUID().toString()
            sp_edit.putString("localUUID",uuid)
            sp_edit.apply()
        }

        val conn = findViewById<Button>(R.id.conn)
        conn.setOnClickListener {
            val pb = findViewById<ProgressBar>(R.id.pb)
            pb.visibility = View.VISIBLE
            thread {
                //启动客户端
                clientConnection("192.168.0.103","8899",this)


                //连接成功
                val sp = getSharedPreferences("model", MODE_PRIVATE)
                val model = sp.getString("model",null)
                //判断是否已经选择过model
                if (model!=null){
                    //如果选择过 则判断哪种客户端
                    if (model=="client"){
                        val intent = Intent(this, ClientActivity::class.java)
                        startActivity(intent)
                    }else{
                        //如果是控制端 判断是否绑定过
                        val sp1 = getSharedPreferences("UUID", MODE_PRIVATE)
                        val clientUUID =  sp1.getString("BindUUID",null)
                        if (clientUUID == null|| clientUUID =="") {
                            //如果没绑定过 则跳转Bind
                            val intent = Intent(this, BindActivity::class.java)
                            startActivity(intent)
                        }else{
                            //绑定过 则直接跳转控制台
                            val intent = Intent(this, ConsoleActivity::class.java)
                            startActivity(intent)
                        }
                    }

                }else{
                    //如没选择过则跳转至switch
                    val intent = Intent(this, SwitchActivity::class.java)
                    startActivity(intent)
                }


//                client = Client(URI("ws://192.168.0.103:8899"), this)
//                client.connect()
            }

        }




    }
}