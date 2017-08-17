package com.amap.map3d.demo.chat_player.util;

import com.amap.map3d.demo.chat_player.bean.New;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *  on 2017/6/20.
 */

public class JsonParseUtil {
    /**
     * 解析JSON字符串
     */
    public static List<New> jsonParse(JSONObject jsonObject, String ss)
    {
        Gson gson = new Gson();
        List<New> news = new ArrayList<>();
        try {
            JSONArray ja = jsonObject.getJSONArray(ss);
            for(int i = 0;i<ja.length();i++)
            {
                news.add(gson.fromJson(ja.getJSONObject(i).toString(),New.class));
            }
            return news;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
