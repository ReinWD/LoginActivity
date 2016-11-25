package com.jay.loginactivitytest.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.jay.loginactivitytest.R;
import com.jay.loginactivitytest.data.User;
import com.jay.loginactivitytest.data.UserManager;

public class LoginActivity extends AppCompatActivity {
    private static final String REMEMBERED = "remembered";

    private EditText mEdtPhone, mEdtPassword;
    private Button mBtnForget, mBtnLogin;
    private Button mBtnRegister;
    private ImageButton mImgBtnBack;
    private RadioButton mRadioButton;

    private UserManager mUserManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserManager = new UserManager(this);

        SharedPreferences preferences = getSharedPreferences("option", MODE_PRIVATE);
        int count = preferences.getInt("count", 0);
        //创建数据文件并存储一个空账户
        if (count == 0) mUserManager.addUser(new User(".", "."));
        //存储程序使用次数
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("count", ++count);
        editor.apply();

        mEdtPhone = (EditText) findViewById(R.id.edt_phone);
        mEdtPassword = (EditText) findViewById(R.id.edt_password);
        mBtnForget = (Button) findViewById(R.id.btn_forget);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mImgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        mRadioButton = (RadioButton) findViewById(R.id.remember_password);

        //记住密码
        if (preferences.getBoolean(REMEMBERED, false)) {
            mRadioButton.setChecked(true);
            mEdtPhone.setText(preferences.getString("phone",""));
            mEdtPassword.setText(preferences.getString("password",""));
        }

        mRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(REMEMBERED, isChecked);
                editor.apply();
            }
        });

        //密码找回
        mBtnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("密码找回");
                builder.setMessage("这么简单的密码你都要忘，笨得可以的");
                final View view = getLayoutInflater().inflate(R.layout.find_password, null);
                builder.setView(view);
                builder.setPositiveButton("查询", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = (EditText) view.findViewById(R.id.editText);
                        String phone = editText.getText().toString();
                        User user = mUserManager.searchUser(phone);
                        if (user != null)
                            Toast.makeText(LoginActivity.this, "你的密码是：" + user.getPassword(), Toast.LENGTH_LONG).show();
                        else Toast.makeText(LoginActivity.this, "没有此账户", Toast.LENGTH_LONG).show();

                    }
                });
                builder.setCancelable(false);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        //登录
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mEdtPhone.getText().toString();
                User user = mUserManager.searchUser(phone);

                //检查账户是否存在
                if (user == null) {
                    Toast.makeText(LoginActivity.this, "没有此账户，请检查账号是否输入正确", Toast.LENGTH_LONG).show();
                    mEdtPhone.clearFocus();
                    mEdtPhone.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(mEdtPhone, 0);
                    return;
                }
                //检查密码是否输入正确
                String password = mEdtPassword.getText().toString();
                if (password.equals(user.getPassword())) {
                    editor.putString("phone", phone);
                    editor.putString("password", password);
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MakeTroubleActivity.class);
                    startActivity(intent);
                    finish();
                } else Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_LONG).show();
            }
        });

        //注册
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        //返回
        mImgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode) {
            mEdtPhone.setText(data.getStringExtra("phone"));
            mEdtPassword.setText(data.getStringExtra("password"));
        }
    }
}
