package com.example.xinan.util;

import com.example.xinan.db.Message;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Utility {
    public static void handleMessageResponse(List<Message> messages,String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("Bangs");
            for (int i = 0; i < jsonArray.length(); i++) {
                String MessageContent = jsonArray.getJSONObject(i).toString();
                Message m = new Gson().fromJson(MessageContent, Message.class);
                messages.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
