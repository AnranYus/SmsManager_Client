package com.lsper.client.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import com.lsper.client.R
import com.lsper.client.bean.Sms

class GetSMSReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val intent = Intent()
        val sms = intent.getStringExtra("sms")
        val smsobj = Gson().fromJson<Sms>(sms,Sms::class.java)
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val channel = NotificationChannel("smsPush","短信通知",NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        val id = (Math.random() * 100).toInt()
        val notification = NotificationCompat.Builder(context,"smsPush")
            .setContentTitle(smsobj.phoneNumber)
            .setContentText(smsobj.smsContent)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_foreground))
            .build()
        manager.notify(id,notification)
    }
}