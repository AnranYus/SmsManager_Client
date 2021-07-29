package com.lsper.client.service

import android.app.Application
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.google.gson.Gson
import com.lsper.client.MyApplication
import com.lsper.client.bean.Content
import java.util.*

class SocketBeatService : Service() {

    override fun onBind(intent: Intent): IBinder {
        return Bear(application)
    }

    class Bear(val application: Application): Binder(){

        fun startBeat(){
            val timer = Timer()
            timer.schedule(MyTimerTask(application),0,1000*60)
        }

    }
    class MyTimerTask(val application: Application) : TimerTask(){
        override fun run() {
            val client = (application as MyApplication).getInstance().socketClient
            if (client.isOpen){
                val uuid = (application as MyApplication).getInstance().getSharedPreferences("UUID",
                    MODE_PRIVATE).getString("localUUID",null)
                val model = (application as MyApplication).getInstance().getSharedPreferences("model",
                    MODE_PRIVATE).getString("model",null)
                val content : Content = Content("Bear","Bear",uuid,model)
                client.send(Gson().toJson(content))
            }
        }

    }
}