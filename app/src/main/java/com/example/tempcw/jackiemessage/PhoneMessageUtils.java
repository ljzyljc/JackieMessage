package com.example.tempcw.jackiemessage;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by TempCw on 2016/11/28.
 */
//读取手机中的最新的验证码
public class PhoneMessageUtils {
    /**
     *
     * @param time  当前时间
     * @param context
     * @param editText 需要填写的Edittext
     */
    public static void getValidateCode(long time, Context context, EditText editText) {
        Log.i("jc","执行了该方法");
        String code = "";
        Uri inboxUri = Uri.parse("content://sms/inbox");
        String where = " date >  " + (time-1000);//60s以内的短信
        Cursor c = context.getContentResolver().query(inboxUri, null, where, null, "date desc");//
        if (c != null) {
            if (c.moveToFirst()) {
                String address = c.getString(c.getColumnIndex("address"));
                String body = c.getString(c.getColumnIndex("body"));

                //13162364720为发件人的手机号码
                if (!address.equals("1065504312110")) {
                    return ;
                }
                Log.i("jc", "发件人为:" + address + " ," + "短信内容为:" + body);

                Pattern pattern = Pattern.compile("(\\d{6})");
                Matcher matcher = pattern.matcher(body);

                if (matcher.find()) {
                    code = matcher.group(0);
                    Log.i("jc", "验证码为: " + code);
//                    Log.i("jc","发送验证码的时间"+time+"  现在的时间"+System.currentTimeMillis()+"    "+time+120*1000);
                    long dd=time+(long)120*1000;
                    if (time< System.currentTimeMillis()) {
                        editText.setText(code);
                    }
//                    mHandler.obtainMessage(MainActivity.MSG_RECEIVED_CODE, code).sendToTarget();
                }

            }
            c.close();
        }
    }
}
