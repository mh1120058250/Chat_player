package com.amap.map3d.demo.chat_player.Index;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.map3d.demo.chat_player.MainActivity;
import com.amap.map3d.demo.chat_player.R;
import com.amap.map3d.demo.chat_player.activity.ActivityRegister;
import com.amap.map3d.demo.chat_player.model.UserModel;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

import static cn.bmob.v3.BmobUser.getCurrentUser;

/**
 * Created by Administrator on 2017/7/18 0018.
 */

public class Loding extends AppCompatActivity {
    private EditText username,pwd;
    public int CODE_NULL=1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        username= (EditText) findViewById(R.id.id_UserName_EditText);
        pwd= (EditText) findViewById(R.id.id_UserPsw_EditText);
        findViewById(R.id.id_Loading_Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              final String name=username.getText().toString();
                final String pwd1=pwd.getText().toString();
                UserModel.getInstance().login(name, pwd1, new LogInListener() {
                    @Override
                    public void done(Object o, BmobException e) {
                        if (e==null)
                        {
                            //toast(name);
                            isFirstRun(name);
                            //toast(pwd1);
                            toast("登录成功！");
                            startActivity(new Intent(Loding.this, MainActivity.class));
                            finish();
                        }else {
                            toast("用户名或密码不正确！");
                        }
                    }

                    @Override
                    public void done(Object o, Object o2) {

                    }
                });
            }
        });
        findViewById(R.id.id_Register_Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Loding.this, ActivityRegister.class));
            }
        });
    }
    //判断是否是第一次运行
    public void isFirstRun(String ss)
    {

        SharedPreferences.Editor sharedata = getSharedPreferences("data", 0).edit();
        sharedata.putString("item",ss);
        sharedata.commit();
    }
    private String isFirstRun1(String ss)
    {
        //SharedPreferences存储共享变量的文件路径位于“/data/data/应用程序包/shared_prefs”目录下
        //获取Preferences,如果没有则自动创建
        SharedPreferences preferences = getSharedPreferences("app",MODE_PRIVATE);
        String isFirst = preferences.getString("first_run",ss);
        //如果是第一次运行
        if (isFirst==null)
        {
            //改变为不是第一次
            preferences.edit().putString("first_run",ss).commit();
        }
        return isFirst;
    }
    private void toast(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

}
