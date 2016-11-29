package com.example.tempcw.jackiemessage;

import android.util.Log;

//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by TempCw on 2016/10/28.
 */

public class HttpUtils {
//
//    public static String getHttpClient(String url){
//        HttpClient httpClient=new DefaultHttpClient();
//        HttpGet httpGet=new HttpGet(url);
//        try {
//            HttpResponse response=httpClient.execute(httpGet);
//            HttpEntity entity=response.getEntity();
//            String str= EntityUtils.toString(entity);
//            return str;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//    public static String get(String urlStr, Map<String, String> paramMap)
//            throws Exception {
//        //拼接参数
//        StringBuilder params = new StringBuilder(urlStr+"?");
//        int i = 0;
//        for (String key : paramMap.keySet()) {
//            String value = paramMap.get(key);
//            params.append(key);
//            params.append("=");
//            params.append(value);
//            if (i < paramMap.size() - 1) {
//                params.append("&");
//            }
//            i++;
//        }
//        return getHttpClient(params.toString());
//    }

    public static String get(String urlStr) {
        String result = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            if (connection.getResponseCode() == 200) {
                InputStream inStream = connection.getInputStream();
                result = new String(readInputStream(inStream));
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len = inStream.read(buffer)) !=-1 ){
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();//网页的二进制数据
        outStream.close();
        inStream.close();
        return data;
    }

    //  表单提交

    public static String post(String urlStr, Map<String, String> paramMap)
            throws Exception {
        StringBuffer sb = null;
        //拼接参数
        StringBuilder params = new StringBuilder();
        int i = 0;
        for (String key : paramMap.keySet()) {
            Object value = paramMap.get(key);
            params.append(key);
            params.append("=");
            params.append(value);
            if (i < paramMap.size() - 1) {
                params.append("&");
            }
            i++;
        }
        //创建请求地址
        URL url = new URL(urlStr);
        //打开连接
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        // 设置参数
        httpConn.setDoOutput(true); // 需要输出
        httpConn.setDoInput(true); // 需要输入
        httpConn.setUseCaches(false); // 不允许缓存
        httpConn.setRequestMethod("POST"); // 设置POST方式连接
        // 设置请求属性
        httpConn.setRequestProperty("Charset", "UTF-8");
        // 连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
        httpConn.connect();
        // 建立输入流，向指向的URL传入参数
        DataOutputStream dos = new DataOutputStream(httpConn.getOutputStream());
        dos.writeBytes(params.toString());
        dos.flush();
        dos.close();
        // 获得响应状态
        int resultCode = httpConn.getResponseCode();
        sb = new StringBuffer();
        if (HttpURLConnection.HTTP_OK == resultCode) {
            //解析服务器返回的数据
            String readLine = new String();
            BufferedReader responseReader = new BufferedReader(
                    new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
            while ((readLine = responseReader.readLine()) != null) {
                sb.append(readLine).append("\n");
            }
            responseReader.close();
            Log.i("jc",sb.toString());
            return sb.toString();
        }
        return null;
    }
}
