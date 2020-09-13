package com.example.xinan.db;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 *暂未使用，为用户系统作准备
 */
public class User {
    private String account;
    private byte[] password;

    public User(String username, byte[] password) {
        this.account = username;
        this.password = password;
    }
}
