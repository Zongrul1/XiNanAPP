package com.example.xinan.util;

import com.example.xinan.db.Content;
import com.example.xinan.db.News;
import com.google.gson.Gson;

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
}
