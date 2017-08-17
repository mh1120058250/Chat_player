package com.amap.map3d.demo.chat_player.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListView;

import com.amap.map3d.demo.chat_player.Adpter.ContactAdapter;
import com.amap.map3d.demo.chat_player.R;
import com.amap.map3d.demo.chat_player.activity.ChatActivity;
import com.amap.map3d.demo.chat_player.activity.Friend_one;
import com.amap.map3d.demo.chat_player.bean.Friend;
import com.amap.map3d.demo.chat_player.bean.User;
import com.amap.map3d.demo.chat_player.event.RefreshEvent;
import com.amap.map3d.demo.chat_player.model.UserModel;
import com.github.promeg.pinyinhelper.Pinyin;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/7/18 0018.
 */

public class Fragments extends Fragment {
    private String text;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private ContactAdapter adapter;
    private List<Friend> friendList;
    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView =inflater.inflate(R.layout.fragment_one,container,false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        initListener();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle data = getArguments();
        text = data !=null?data.getString("text"):"失败！";
        super.onCreate(savedInstanceState);
    }
    private void initListener() {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                swipeRefreshLayout.setRefreshing(true);
                query();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {//跳转到新朋友页面
                    getContext().startActivity(new Intent(getContext(),Friend_one.class));
                } else {
                    Friend friend = adapter.getItem(i);
                    User user = friend.getFriendUser();
                    BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
                    //启动一个会话，实际上就是在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
                    BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info,true,null);
                    Intent intent = new Intent();
                    intent.setClass(getContext(), ChatActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("c", c);
                    if (bundle != null) {
                        intent.putExtra(getContext().getPackageName(), bundle);
                    }
                    startActivity(intent);
                }
            }
        });
    }
    private void initDatas() {
        adapter = new ContactAdapter(getActivity(),friendList);
        listView.setAdapter(adapter);
    }

    private void initViews() {
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.id_sw_refresh);
        listView = (ListView) getView().findViewById(R.id.id_conversion_listView);

    }
    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.setRefreshing(true);
        query();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        //重新刷新列表
        loge("---联系人界面接收到自定义消息---");
        adapter.notifyDataSetChanged();
    }

    private void loge(String str) {
        Log.e("contact",str);
    }

    /**
     * 查询好友
     */
    public void query() {
        UserModel.getInstance().queryFriends(
                new FindListener<Friend>() {
                    @Override
                    public void done(List<Friend> list, BmobException e) {
                        if(list!=null){
                        if (e == null ) {
                            List<Friend> friends = new ArrayList<Friend>();
                            friends.clear();
                            //添加首字母
                            for (int i = 0; i < list.size(); i++) {
                                for (int j = i+1; j < list.size(); j++) {
                                    if(list.get(i).getFriendUser().getUsername().equals(list.get(j).getFriendUser().getUsername())){
                                        Friend friend = list.get(i);
                                        UserModel.getInstance().deleteFriend(friend, new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {

                                            }
                                        });
                                    }
                                }

                            }
                            for (int i = 0; i < list.size(); i++) {
                                Friend friend = list.get(i);
                                String username = friend.getFriendUser().getUsername();
                                String pinyin = Pinyin.toPinyin(username.charAt(0));
                                friend.setPinyin(pinyin.substring(0, 1).toUpperCase());
                                friends.add(friend);
                            }
                            friendList = friends;
                            initDatas();
                            Log.e("-------->",friendList.size()+"");
                            adapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            friendList = null;
                            initDatas();
                            adapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                            Logger.i(e.getMessage() + "--" + e.getErrorCode());
                        }
                        }else{

                           // Toast.makeText(getContext(), "好友为空", Toast.LENGTH_SHORT).show();
                    }
                    }
                }


        );
    }

}
