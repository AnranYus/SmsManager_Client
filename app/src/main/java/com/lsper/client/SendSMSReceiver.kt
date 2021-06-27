package com.lsper.client

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.util.Log
import com.google.gson.Gson
import com.lsper.client.bean.Sms

class SendSMSReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.e("sms","Send")
        val smsManager = SmsManager.getDefault()
        val smsJson = intent.getStringExtra("sms")
        val gson = Gson()
        val sms = gson.fromJson(smsJson, Sms::class.java)
        if (sms.phoneNumber!=null){
            if (sms.lateTime!=null){
                Thread.sleep(sms.lateTime)
                smsManager.sendTextMessage(sms.phoneNumber,null,sms.smsContent,null,null)
            }else{
                smsManager.sendTextMessage(sms.phoneNumber,null,sms.smsContent,null,null)
            }
        }
    }
}