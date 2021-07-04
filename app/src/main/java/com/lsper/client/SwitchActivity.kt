package com.lsper.client

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.lsper.client.activity.client.ClientActivity
import com.lsper.client.activity.console.BindActivity

class SwitchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch)

    }

    fun radioChecked(view: View){
        val spe = getSharedPreferences("model", MODE_PRIVATE).edit()
        //判断选择的客户端类型
        if (view is RadioButton){
            when(view.id){
                R.id.toClient -> {
                    if (ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS),1)
                    }else {
                        val intent = Intent(this, ClientActivity::class.java)
                        startActivity(intent)
                        spe.putString("model", "client")
                    }
                }
                R.id.toConsole -> {
                    val intent = Intent(this, BindActivity::class.java)
                    startActivity(intent)
                    spe.putString("model","console")
                }
            }
            spe.apply()
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
                if (!(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)){
                    Toast.makeText(this,"那我走？",Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}