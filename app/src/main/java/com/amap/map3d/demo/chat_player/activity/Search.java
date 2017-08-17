package com.amap.map3d.demo.chat_player.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.map3d.demo.chat_player.Adpter.ActivitySearchAdapter;
import com.amap.map3d.demo.chat_player.R;
import com.amap.map3d.demo.chat_player.bean.User;
import com.amap.map3d.demo.chat_player.model.BaseModel;
import com.amap.map3d.demo.chat_player.model.UserModel;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/7/18 0018.
 */

public class Search extends Activity {
    private EditText searchUserEditText;
    private Button searchUserBtn;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private ActivitySearchAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        initView();
        adapter = new ActivitySearchAdapter(this);
        listView.setAdapter(adapter);
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Bundle bundle = new Bundle();
                User user = adapter.getItem(position);
                bundle.putSerializable("u", user);
                Intent intent = new Intent(Search.this,ActivityUserInfo.class);
                intent.putExtra(getPackageName(),bundle);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        searchUserEditText = (EditText) findViewById(R.id.id_SearchUser_EditText);
        searchUserBtn = (Button) findViewById(R.id.id_SearchUser_Btn);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_sw_refresh);
        listView = (ListView) findViewById(R.id.id_Search_ListView);
        searchUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeRefreshLayout.setRefreshing(true);
                query();
            }
        });
    }

    private void query() {
        final String name = searchUserEditText.getText().toString();
        if (TextUtils.isEmpty(name))
        {
            toast("请输入用户名！");
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        UserModel.getInstance().queryUsers(name, BaseModel.DEFAULT_LIMIT, new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e==null)
                {
                    ArrayList<User> users = users = new ArrayList<User>();
                    for (User user:list) {
                        //toast(user.getUsername());
                        if (user.getUsername().contains(name))
                        {
                            users.add(user);
                            toast(user.getUsername());
                        }
                    }
                    swipeRefreshLayout.setRefreshing(false);
                    adapter.setDatas(users);
                    adapter.notifyDataSetChanged();
                    toast("查无此人！");
                }else
                {
                    toast(e.getMessage());
                    swipeRefreshLayout.setRefreshing(false);
                    adapter.setDatas(null);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void toast(String str) {
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

}
