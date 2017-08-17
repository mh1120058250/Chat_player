package com.amap.map3d.demo.chat_player.Adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/7/18 0018.
 */

public class MyFragmentViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<String> tab;
    public MyFragmentViewPagerAdapter(FragmentManager fm,List<Fragment> fragments,List<String> tab) {
        super(fm);
        this.fragments = fragments;
        this.tab = tab;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return tab.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tab.get(position);
    }


}
