package com.jay.loginactivitytest.data;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Created by Jay on 2016/11/23.
 */

public class UserManager {
    private final String FILE_NAME = "account.dat";
    private Context mContext;

    public UserManager(Context context) {
        mContext = context;
    }

    public void addUser(User user) {
        PrintStream out = null;
        try {
            out = new PrintStream(mContext.openFileOutput(FILE_NAME, mContext.MODE_APPEND));
            out.println(user.PHONE + ":" + user.getPassword());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    public User searchUser(String phone) {
        Scanner in = null;
        User user = null;
        try {
            in = new Scanner(mContext.openFileInput(FILE_NAME));
            while (in.hasNext()) {
                String[] strs = in.nextLine().split(":");
                if (strs[0].equals(phone)) {
                    user = new User(strs[0], strs[1]);
                    return user;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (in != null)
                in.close();
        }
        return user;
    }
}
