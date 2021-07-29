package com.lsper.client.service

import android.app.Service
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.IBinder
import android.util.Log
import com.lsper.client.MyApplication
import com.lsper.client.observer.SmsObserver


class GetSMSService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        registerSmsDatabaseChangeObserver(this)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterSmsDatabaseChangeObserver(this)
    }

    val SMS_MESSAGE_URI: Uri = Uri.parse("content://sms")
    private var mSmsDBChangeObserver: SmsObserver? = null
    private fun registerSmsDatabaseChangeObserver(contextWrapper: ContextWrapper) {

        try {
            mSmsDBChangeObserver =
                SmsObserver(this.contentResolver, Handler(),application as MyApplication)
            mSmsDBChangeObserver?.let {
                contextWrapper.contentResolver.registerContentObserver(
                    SMS_MESSAGE_URI,
                    true,
                    it
                )
            }
        } catch (b: Throwable) {
        }
    }

    private fun unregisterSmsDatabaseChangeObserver(contextWrapper: ContextWrapper) {
        try {
            mSmsDBChangeObserver?.let { contextWrapper.contentResolver.unregisterContentObserver(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}