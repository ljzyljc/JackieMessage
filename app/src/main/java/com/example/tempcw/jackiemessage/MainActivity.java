package com.example.tempcw.jackiemessage;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Map<String,String> map;
    private SMSContentObserver smsContentObserver;
    private SmsReceiveBroadcastReceiver broadcastReceiver;
    Handler handler = new Handler()
    {
        @Override
        public void dispatchMessage(Message msg) {
            if (msg.what == 1)
            {
                Log.i("jc",msg.obj.toString()+"--1--");
               // code.setText(msg.obj.toString());
            }
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //=============第一种方案=================
//        setContentView(R.layout.activity_main);
//        smsContentObserver=new SMSContentObserver(this,handler,6);
////        Uri inboxUri = Uri.parse("content://sms/");
//        Log.i("jc","content://sdddd4ms/");
//        MainActivity.this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true ,smsContentObserver);
        //=============第二种方案==================
        broadcastReceiver=new SmsReceiveBroadcastReceiver(MainActivity.this,handler,6);
        IntentFilter intentFilter=new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(broadcastReceiver,intentFilter);
        Log.i("jc","注册了");

        //==============第三种方案=====================（使用方法来搜索手机中的短信）
//        PhoneMessageUtils.getValidateCode();



//        map=new HashMap<String,String>();
//        map.put("expire","180");
//        map.put("tel","13182676063");
//        map.put("type","3");
//        map.put("content","");
    }
//    public void send(View v){
//        try {
//            HttpUtils.post("https://gaapi.jl.gov.cn:443/econsole/api/sms/send", map);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
