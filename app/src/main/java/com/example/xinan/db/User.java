package com.example.xinan.db;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    private String account;
    private byte[] password;

    public User(String username, byte[] password) {
        this.account = username;
        this.password = password;
    }
}
