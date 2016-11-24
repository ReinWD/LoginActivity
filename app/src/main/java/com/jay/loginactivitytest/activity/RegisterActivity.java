package com.jay.loginactivitytest.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jay.loginactivitytest.R;
import com.jay.loginactivitytest.data.User;
import com.jay.loginactivitytest.data.UserManager;

public class RegisterActivity extends AppCompatActivity {
    private EditText mEdtPhone, mEdtPassword, mEdtConfirmPassword;
    private Button mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEdtPhone = (EditText) findViewById(R.id.edt_phone);
        mEdtPassword = (EditText) findViewById(R.id.edt_password);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mEdtConfirmPassword = (EditText) findViewById(R.id.edt_confirmPassword);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检查手机号
                String phoneNumber = mEdtPhone.getText().toString();
                if (phoneNumber.length() != 11) {
                    Toast.makeText(RegisterActivity.this, "请检查手机号是否输入正确，仅支持11位手机号", Toast.LENGTH_LONG).show();
                    mEdtPhone.clearFocus();
                    mEdtPhone.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(mEdtPhone, 0);
                    return;
                }
                //检查密码长度
                String password = mEdtPassword.getText().toString();
                if (password.length() < 8) {
                    Toast.makeText(RegisterActivity.this, "密码长度应为：8 - 12", Toast.LENGTH_LONG).show();
                    mEdtPassword.clearFocus();
                    mEdtPassword.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(mEdtPassword, 0);
                    return;
                }
                //检查密码是否一致
                String confirmPassword = mEdtConfirmPassword.getText().toString();
                if (confirmPassword.equals(password)) {
                    //检查账户是否已存在
                    UserManager userManager = new UserManager(RegisterActivity.this);
                    if (userManager.searchUser(phoneNumber) == null) {
                        userManager.addUser(new User(phoneNumber, password));
                        Toast.makeText(RegisterActivity.this, "注册成功，现在你可以登录了", Toast.LENGTH_LONG).show();
                    }
                    else Toast.makeText(RegisterActivity.this, "账户已存在可直接登录", Toast.LENGTH_LONG).show();
                    //回到登录界面
                    Intent data = new Intent();
                    data.putExtra("phone", phoneNumber);
                    data.putExtra("password", password);
                    setResult(0, data);
                    finish();
                }else {
                    Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_LONG).show();
                    mEdtConfirmPassword.clearFocus();
                    mEdtConfirmPassword.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(mEdtConfirmPassword, 0);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            setResult(1);
        return super.onKeyDown(keyCode, event);
    }
}
