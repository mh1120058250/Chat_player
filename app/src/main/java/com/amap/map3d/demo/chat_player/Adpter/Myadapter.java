package com.amap.map3d.demo.chat_player.Adpter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.amap.map3d.demo.chat_player.R;
import com.amap.map3d.demo.chat_player.activity.ActivityUserInfo;
import com.amap.map3d.demo.chat_player.activity.Search;
import com.amap.map3d.demo.chat_player.bean.User;

import java.util.List;

/**
 * Created by Administrator on 2017/7/18 0018.
 */

public class Myadapter extends BaseAdapter {
    /**
     * 数据源
     */
    private List<String> datas;
    private Context context;
    public Myadapter(List<String> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }
    //返回数据源的长度
    @Override
    public int getCount() {
        return datas.size();
    }
    //返回指定位置的item对应的实体对象
    @Override
    public String getItem(int position) {
        return datas.get(position);
    }
    //返回当前item的位置
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 一个item需要显示数据，首先要有个item视图
     * 这个item视图就是通过调用这个方法获取到的
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //得到LayoutInflater对象
        LayoutInflater inflater = LayoutInflater.from(context);
        //使用LayoutInflater加载一个布局文件
        View view = inflater.inflate(R.layout.item,null);
        //找到控件
        Button btn= (Button) view.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ActivityUserInfo.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        TextView tv = (TextView) view.findViewById(R.id.tv);
        tv.setText(getItem(position));
        return view;
    }
}

