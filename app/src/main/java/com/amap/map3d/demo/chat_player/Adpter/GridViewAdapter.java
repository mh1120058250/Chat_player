package com.amap.map3d.demo.chat_player.Adpter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.amap.map3d.demo.chat_player.R;

import java.util.List;

/**
 * Created by Administrator on 2017/7/25 0025.
 */

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private List listitem;

    public GridViewAdapter(Context context,List listitem) {
        this.context = context;
        this.listitem = listitem;
    }

    @Override
    public int getCount() {
        return listitem.size();
    }

    @Override
    public Object getItem(int position) {
        return listitem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int s= (int) listitem.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.gridview_item, null);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        imageView.setImageResource(s);
        return convertView;
    }

}