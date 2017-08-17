package com.amap.map3d.demo.chat_player.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.map3d.demo.chat_player.R;
import com.amap.map3d.demo.chat_player.bean.Url;
import com.amap.map3d.demo.chat_player.util.HttpUtil;
import com.amap.map3d.demo.chat_player.view.SwipeRefreshView;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/16 0016.
 */

public class Fragment_message extends Fragment {
    private String text;
    private Map url;
    private int index;
    private ListView listView;
    private SwipeRefreshView swipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.messages_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView= (ListView) getView().findViewById(R.id.listview);
        HttpUtil.volleyGetJson(getContext(),listView);
        swipeRefreshLayout = (SwipeRefreshView)getView(). findViewById(R.id.srl);
//        HttpUtil.volleyGetJson(getContext(),listView);
//
//            HttpUtil.volleyGetJson(getContext(),listView);
//
//        //设置下拉进度的背景颜色，默认是白色
//        //swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.holo_blue_light);
//        //设置下拉进度的主题颜色
//        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.bg_color,R.color.colorAccent,R.color.colorPrimaryDark);
//        //下拉刷新监听事件
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            //正在刷新
//            @Override
//            public void onRefresh() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        HttpUtil.volleyGetJson(getContext(),listView);
//                        Toast.makeText(getContext(),"数据已刷新",Toast.LENGTH_SHORT).show();
//                        //加载完数据后设置为不刷新状态，将下拉进度条收起
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                },1000);
//            }
//        });
//        //上拉加载
//        swipeRefreshLayout.setOnLoadListener(new SwipeRefreshView.OnLoadListener() {
//            @Override
//            public void onLoad() {
//                index+=20;
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        HttpUtil.pushVolleyGetJson(getContext(),listView,index,text);
//                        Toast.makeText(getContext(),"加载了20条数据",Toast.LENGTH_SHORT).show();
//                        //加载完成后设置为不加载状态，将加载进度收起来
//                        swipeRefreshLayout.setLoading(false);
//                    }
//                },2000);
//            }
//        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    HttpUtil.volleyGetJson(getContext(),listView);
                    Toast.makeText(getContext(),"数据已刷新",Toast.LENGTH_SHORT).show();
                    //加载完数据后设置为不刷新状态，将下拉进度条收起
                    //swipeRefreshLayout.setRefreshing(false);
                }
            },0);
        }
    }


}
