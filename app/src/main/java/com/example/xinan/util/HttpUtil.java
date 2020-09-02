package com.example.xinan.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    private static String token = "s1F29wCh3E4DmiEwYGyl0efNB7KvB0o6SjqVajSFtWPYYXvnGRVqPRn9XCB+xuZL";;
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).addHeader("Token", token).build();
        client.newCall(request).enqueue(callback);
    }

}
