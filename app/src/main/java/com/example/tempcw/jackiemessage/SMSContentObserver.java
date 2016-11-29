package com.example.tempcw.jackiemessage;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by TempCw on 2016/11/29.
 */

public class SMSContentObserver extends ContentObserver {
    private Context mContext;
    private Handler mHandler;
    private String compileValue;
    private Uri mUri = Uri.parse("content://sms/inbox");

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    //验证码的长度,一个数字
    public SMSContentObserver(Context context, Handler handler, int codeLength) {
        super(handler);
        mContext=context;
        mHandler=handler;
        compileValue = "\\d{" + codeLength + "}";
        Log.i("jc","创建了SMSContentObserver");
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Log.i("jc","执行了第一个onchange方法");
    }
    @Override
    public void onChange(boolean selfChange, Uri uri) {
        Log.i("jc","执行了onChange");
        if(uri.toString().equals("content://sms/raw")){
            return;
        }
        Cursor cursor=mContext.getContentResolver().query(uri,null,null,null,null);
        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String body = cursor.getString(cursor.getColumnIndex("body"));
                Log.i("jc", "get sms:address:" + address + "body:" + body);
                // 在这里我们的短信提供商的号码如果是固定的话.我们可以再加一个判断,这样就不会受到别的短信应用的验证码的影响了
                // 不然的话就在我们的正则表达式中,加一些自己的判断,例如短信中含有自己应用名字啊什么的...
                // if (!address.equals("13676900000")) {
                // return;
                // }
                Pattern pattern = Pattern.compile(compileValue);
                Matcher matcher = pattern.matcher(body);
                if (matcher.find()) {

                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = matcher.group(0);
                    mHandler.sendMessage(msg);
                } else {
                    Log.i("jc", "没有在短信中获取到合格的验证码");
                }


            }
        }
        cursor.close();
    }




















}
