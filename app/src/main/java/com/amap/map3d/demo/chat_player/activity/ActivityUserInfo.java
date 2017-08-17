package com.amap.map3d.demo.chat_player.activity;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.map3d.demo.chat_player.R;
import com.amap.map3d.demo.chat_player.bean.AddFriendMessage;
import com.amap.map3d.demo.chat_player.bean.User;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**
 *  2017/7/19.
 * 用户资料
 */

public class ActivityUserInfo extends Activity{
    private TextView tvName;
    private ImageView ivAvator;
    private Button btnAddFriend;
    private User user;
    private BmobIMUserInfo info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
        initData();
    }

    private void initData() {
        user=(User)getBundle().getSerializable("u");
        if(user.getObjectId().equals(getCurrentUid())){
            btnAddFriend.setVisibility(View.GONE);
            //btn_chat.setVisibility(View.GONE);
        }else{
            btnAddFriend.setVisibility(View.VISIBLE);
            //btn_chat.setVisibility(View.VISIBLE);
        }
        //构造聊天方的用户信息:传入用户id、用户名和用户头像三个参数
        info = new BmobIMUserInfo(user.getObjectId(),user.getUsername(),user.getAvatar());
        //ImageLoaderFactory.getLoader().loadAvator(ivAvator,user.getAvatar(),R.mipmap.ic_launcher);
        tvName.setText(user.getUsername());
    }
    //返回Bundle值
    public Bundle getBundle() {
        if (getIntent() != null && getIntent().hasExtra(getPackageName()))
            return getIntent().getBundleExtra(getPackageName());
        else
            return null;
    }
    //获取用户ID
    public String getCurrentUid(){
        return BmobUser.getCurrentUser(User.class).getObjectId();
    }
    private void initView() {
        tvName = (TextView) findViewById(R.id.tv_name);
        ivAvator = (ImageView) findViewById(R.id.iv_avator);
        btnAddFriend = (Button) findViewById(R.id.btn_add_friend);
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAddFriendMessage();
            }
        });
    }
    /**
     * 发送添加好友的请求
     */
    private void sendAddFriendMessage(){
        //启动一个会话，如果isTransient设置为true,则不会创建在本地会话表中创建记录，
        //设置isTransient设置为false,则会在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, true,null);
        //这个obtain方法才是真正创建一个管理消息发送的会话
        BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
        AddFriendMessage msg =new AddFriendMessage();
        User currentUser = BmobUser.getCurrentUser(User.class);
        msg.setContent("很高兴认识你，可以加个好友吗?");//给对方的一个留言信息
        Map<String,Object> map =new HashMap<>();
        map.put("name", currentUser.getUsername());//发送者姓名，这里只是举个例子，其实可以不需要传发送者的信息过去
        map.put("avatar",currentUser.getAvatar());//发送者的头像
        map.put("uid",currentUser.getObjectId());//发送者的uid
        msg.setExtraMap(map);
        conversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                if (e == null) {//发送成功
                    toast("好友请求发送成功，等待验证");
                } else {//发送失败
                    toast("发送失败:" + e.getMessage());
                }
            }
        });
    }

    private void toast(String str) {
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
}
