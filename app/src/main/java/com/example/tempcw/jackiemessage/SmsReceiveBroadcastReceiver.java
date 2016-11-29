package com.example.tempcw.jackiemessage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by TempCw on 2016/11/29.
 */

public class SmsReceiveBroadcastReceiver extends BroadcastReceiver{
    private Context context;
    private Handler handler;
    private int codeLenth=0;

    public SmsReceiveBroadcastReceiver(Context context,Handler handler,int codeLenth){
        this.context=context;
        this.handler=handler;
        this.codeLenth=codeLenth;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("jc","收到了");
        //---接收传入的消息---
        Bundle bundle=intent.getExtras();
        //---查询到达的消息---
        Object[] objects = (Object[]) bundle.get("pdus");
        if (objects!=null){
            String phone=null;
            StringBuffer content = new StringBuffer();
            for (int i = 0; i < objects.length; i++) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) objects[i]);
                phone = sms.getDisplayOriginatingAddress();
                content.append(sms.getDisplayMessageBody());
            }
            Log.i("jc", "phone:" + phone + "\ncontent:" + content.toString());
            checkCodeAndSend(content.toString());
        }

    }
    private void checkCodeAndSend(String content) {
        // 话说.如果我们的短信提供商的短信号码是固定的话.前面可以加一个判断
        // 正则表达式验证是否含有验证码
        Pattern pattern = Pattern.compile("\\d{" + codeLenth + "}");// compile的是规则
        Matcher matcher = pattern.matcher(content);// matcher的是内容
        if (matcher.find()) {
            String code = matcher.group(0);
            Log.i("jc", "短信中找到了符合规则的验证码:" + code);
            handler.obtainMessage(1, code).sendToTarget();
            Log.i("jc", "广播接收器接收到短信的时间:" + System.currentTimeMillis());
        } else {
            Log.i("jc", "短信中没有找到符合规则的验证码");
        }
    }
}
