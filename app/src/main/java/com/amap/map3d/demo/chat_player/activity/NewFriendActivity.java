package com.amap.map3d.demo.chat_player.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.amap.map3d.demo.chat_player.Adpter.NewFriendAdapter;
import com.amap.map3d.demo.chat_player.R;
import com.amap.map3d.demo.chat_player.db.NewFriend;
import com.amap.map3d.demo.chat_player.db.NewFriendManager;

import java.util.List;

/**
 *  2017/7/25.
 */

public class NewFriendActivity extends Activity{
    private List<NewFriend> list;
    private NewFriendAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_conversation);
        list = NewFriendManager.getInstance(this).getAllNewFriend();
        initViews();
        initDatas();

        setListener();
    }

    private void setListener() {
        linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                linearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
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
    }
    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.setRefreshing(true);
        query();
    }
    private void initDatas() {
        //批量更新未读未认证的消息为已读状态
        NewFriendManager.getInstance(this).updateBatchStatus();
        adapter = new NewFriendAdapter(this,list);
        listView.setAdapter(adapter);
    }

    private void initViews() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_sw_refresh);
        listView = (ListView) findViewById(R.id.id_conversion_listView);
        linearLayout = (LinearLayout) findViewById(R.id.ll_root);
    }
    /**
     查询本地会话
     */
    public void query(){
        list = NewFriendManager.getInstance(this).getAllNewFriend();
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}
