package com.example.xinan.util;

import com.example.xinan.db.Content;
import com.example.xinan.db.News;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Utility {
    public static void handleNewsResponse(List<News> news, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("Bangs");
            for (int i = 0; i < jsonArray.length(); i++) {
                String MessageContent = jsonArray.getJSONObject(i).toString();
                News m = new Gson().fromJson(MessageContent, News.class);
                news.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void handleContentResponse(List<Content> cons, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("Data");
            for (int i = 0; i < jsonArray.length(); i++) {
                String MessageContent = jsonArray.getJSONObject(i).toString();
                Content m = new Gson().fromJson(MessageContent, Content.class);
                cons.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Content handleContentResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response).getJSONObject("Data");
            String MessageContent = jsonObject.toString();
            System.out.println(MessageContent);
            return new Gson().fromJson(MessageContent, Content.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String ContentToJson(Content con){
        try{
            //当使用gson把实体转换成json时，如果实体中存在字段的值为NULL的话，那么转换出来的json字符串中将不存在对应的字段，这里就不应该使用Gson gson = new Gson();
            Gson gson = new GsonBuilder().serializeNulls().create();
            String obj=gson.toJson(con);
            return obj;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
