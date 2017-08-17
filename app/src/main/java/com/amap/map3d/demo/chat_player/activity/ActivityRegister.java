package com.amap.map3d.demo.chat_player.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.map3d.demo.chat_player.Index.Loding;
import com.amap.map3d.demo.chat_player.R;
import com.amap.map3d.demo.chat_player.model.UserModel;
import com.amap.map3d.demo.chat_player.util.StringUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * 2017/7/17.
 */

public class ActivityRegister extends Activity{
    //声明控件
    private EditText userNameEditText,userPswEditText,userConfirmPswEditText;
    private Button registerBtn;
    //存储用户输入的数据
    private String userName,userPsw,userConfirmPsw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        userNameEditText = (EditText) findViewById(R.id.id_UserName_EditText);
        userPswEditText = (EditText) findViewById(R.id.id_UserPsw_EditText);
        userConfirmPswEditText = (EditText) findViewById(R.id.id_UserConfirmPsw_EditText);
        registerBtn = (Button) findViewById(R.id.id_Register_Btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取用户输入的数据
                userName = userNameEditText.getText().toString();
                userPsw = userPswEditText.getText().toString();
                userConfirmPsw = userConfirmPswEditText.getText().toString();
                if (!StringUtils.checkUserName(userName))
                {
                    Toast.makeText(ActivityRegister.this,"用户名必须由字母和数字组成，且字母必须开头，长度在3~20之间"
                            ,Toast.LENGTH_SHORT).show();
                }else if (!StringUtils.checkUserPsw(userPsw))
                {
                    Toast.makeText(ActivityRegister.this,"密码必须由数字组成，长度在3~20之间"
                            ,Toast.LENGTH_SHORT).show();
                }else if (!userPsw.equals(userConfirmPsw))
                {
                    Toast.makeText(ActivityRegister.this,"密码和确认密码不一致！"
                            ,Toast.LENGTH_SHORT).show();
                }else {
                    UserModel.getInstance().register(userName, userPsw, userConfirmPsw, new LogInListener() {
                        @Override
                        public void done(Object o, BmobException e) {
                            if (e==null)
                            {
                                toast("注册成功！");
                                startActivity(new Intent(ActivityRegister.this, Loding.class));
                                finish();
                            }else {
                                toast("该用户已存在！");
                            }
                        }

                        @Override
                        public void done(Object o, Object o2) {

                        }
                    });
                }
            }
        });
    }

    private void toast(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
}
