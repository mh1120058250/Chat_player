package com.amap.map3d.demo.chat_player.util;

import com.amap.map3d.demo.chat_player.bean.ChatMessage;
import com.amap.map3d.demo.chat_player.bean.Result;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/14 0014.
 */

public class HttpUtils {
    private static final String URL = "http://www.tuling123.com/openapi/api";
    private static final String API_KEY="422d7020b8dc4478bd0112fe03ce7c48";
    public static ChatMessage sendMesage(String msg){
        ChatMessage chatMessage=new ChatMessage();
        //从json请求道的数据
        String Jsonpro=doGet(msg);
        Gson gson=new Gson();
        Result result=null;
        try{
            result = gson.fromJson(Jsonpro,Result.class);
            //发送
            chatMessage.setMsg(result.getText());
        }catch (Exception e) {
            chatMessage.setMsg("服务器忙！请稍候再试");
        }
        //时间
        chatMessage.setDate(new Date());
        //消息类型
        chatMessage.setType(ChatMessage.Type.INCOMING);
        return  chatMessage;
    }

    private static String doGet(String msg) {
        String result="";
        String url=setParm(msg);
        ByteArrayOutputStream baos = null;
        InputStream is = null;
        try {
            java.net.URL urlNet=new URL(url);
            HttpURLConnection conn=(HttpURLConnection) urlNet.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            is = conn.getInputStream();
            int len = -1;
            byte[] buf = new byte[128];
            baos = new ByteArrayOutputStream();
            while ((len = is.read(buf))!=-1)
            {
                baos.write(buf,0,len);
            }
            baos.flush();
            result = new String(baos.toByteArray());
            //关流
            baos.close();
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String setParm(String msg) {
        String url = "";
        try {
            url = URL+"?key="+API_KEY+"&info="
                    + URLEncoder.encode(msg,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
}
