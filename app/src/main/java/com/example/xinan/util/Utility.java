package com.example.xinan.util;

import com.example.xinan.db.Content;
import com.example.xinan.db.News;
import com.example.xinan.db.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    public static String UserToJson(){
        try{
            //当使用gson把实体转换成json时，如果实体中存在字段的值为NULL的话，那么转换出来的json字符串中将不存在对应的字段，这里就不应该使用Gson gson = new Gson();
            User user = new User("RU",SHA("123456"));
            Gson gson = new GsonBuilder().serializeNulls().create();
            String obj=gson.toJson(user);
            return obj;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] SHA(final String strText) {
        // 是否是有效字符串
        if (strText != null && strText.length() > 0) {
            try {
                // SHA 加密开始
                // 创建加密对象 并傳入加密類型
                String strType = "SHA-256";
                MessageDigest messageDigest = MessageDigest.getInstance(strType);
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes());
                // 得到 byte 類型结果
                return messageDigest.digest();

            } catch (NoSuchAlgorithmException e) {
                return null;
            }
        } else {
            return null;
        }
    }
}
