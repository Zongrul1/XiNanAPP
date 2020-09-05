package com.example.xinan.util;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static String cookie;
    public static void setToken(String token){
        cookie = token;
    }
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).addHeader("Cookie", cookie).build();
        client.newCall(request).enqueue(callback);
    }

    public static void postOkHttpRequest(String address,okhttp3.Callback callback) {
        RequestBody formBody = new FormBody.Builder()
                .add("account", "test")
                .add("password", "test")
                .build();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).post(formBody).build();
        client.newCall(request).enqueue(callback);
    }
}
