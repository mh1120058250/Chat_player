package com.amap.map3d.demo.chat_player.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.map3d.demo.chat_player.R;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.exception.BmobException;

import static com.amap.map3d.demo.chat_player.R.id.info;

/**
 * Created by Administrator on 2017/7/27 0027.
 */

public class Friend_one extends Activity {
    private TextView tv;
    private Button btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one);
        tv= (TextView) findViewById(R.id.tv_name);
        String name=getIntent().getStringExtra("name");
        tv.setText(name);
        btn= (Button) findViewById(R.id.btn_chat);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果需要更新用户资料，开发者只需要传新的info进去就可以

            }
        });
    }
}
