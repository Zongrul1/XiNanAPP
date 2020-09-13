package com.example.xinan.util;

import android.app.Application;

public class XinanApplication extends Application {
    private static String cookie;

    public static String getCookie() {
        return cookie;
    }

    public static void setCookie(String ck) {
        cookie = ck;
    }
}
