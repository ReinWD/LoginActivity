package com.jay.loginactivitytest.data;

/**
 * Created by Jay on 2016/11/23.
 */

public class User {
    public final String PHONE;
    private String mPassword;

    public User(String phone, String password) {
        PHONE = phone;
        mPassword = password;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getPhone() {
        return PHONE;
    }

    public String getPassword() {
        return mPassword;
    }
}
