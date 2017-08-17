package com.amap.map3d.demo.chat_player.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.amap.map3d.demo.chat_player.Adpter.MyFragmentViewPagerAdapter;
import com.amap.map3d.demo.chat_player.Fragments.Fragments;
import com.amap.map3d.demo.chat_player.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/16 0016.
 */

public class Fragment_people extends Fragment {
    private ViewPager viewPager;
    //声明Fragment集合
    private List<Fragment> fragments;
    private List<String> title;
    private TabLayout tableLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.people_fragments,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        viewPager= (ViewPager) getView().findViewById(R.id.viewpager);
        tableLayout= (TabLayout) getView().findViewById(R.id.tablayout);
        title=new ArrayList<>();
        title.add("好友");
        title.add("群");
        title.add("通讯录");
        title.add("多人聊天");
        fragments = new ArrayList<>();
        //初始化Fragment
        for (int i = 0;i< title.size();i++)
        {
            Bundle data = new Bundle();
            data.putString("text", title.get(i));
            Fragments newFragment = new Fragments();
            //通过setArguments来传递数据
            newFragment.setArguments(data);
            fragments.add(newFragment);
        }
        tableLayout.setTabMode(TabLayout.MODE_FIXED);
        for(int i=0;i<title.size();i++){
            tableLayout.addTab(tableLayout.newTab().setText(title.get(i)));
        }
        //创建适配器
        MyFragmentViewPagerAdapter adapter =
                new MyFragmentViewPagerAdapter(getChildFragmentManager(),fragments,title);
        //添加适配器
        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);
    }
}
