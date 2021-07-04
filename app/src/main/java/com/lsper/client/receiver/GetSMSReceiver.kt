package com.lsper.client.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log

class GetSMSReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.e("sms","getsms")
//        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
//        val content = StringBuilder()
//        val sender = StringBuilder()
//        val bundle = intent.extras
//        val format = intent.getStringExtra("format")
//        if (bundle != null){
//            val pdus = bundle["pdus"] as Array<Any>?
//            if (pdus != null) {
//                for (obj:Any in pdus){
//
//                }
//            }
//        }
    }
}