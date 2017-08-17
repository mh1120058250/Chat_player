package com.amap.map3d.demo.chat_player.Adpter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.map3d.demo.chat_player.R;
import com.amap.map3d.demo.chat_player.activity.New_one;
import com.amap.map3d.demo.chat_player.bean.New;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 *  2017/6/21.
 */

public class MyListViewAdapter1 extends BaseAdapter {
    private List<New> news;
    private LayoutInflater inflater;
    //private BitmapCache bitmapCache;
    public MyListViewAdapter1(List<New> news, Context context) {
        this.news = news;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public Object getItem(int position) {
        return news.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder{
        private ImageView img;
        private TextView title,content;
    }
    ViewHolder holder = null;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null)
        {
            convertView = inflater.inflate(R.layout.item,null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.img = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        final  New n = news.get(position);
        holder.title.setText(n.getTitle());
        holder.content.setText(n.getSource());
        holder.img.setImageResource(R.mipmap.ic_launcher);
        //bitmapCache.displayImg(n.getImgsrc(),holder.img);
        Glide.with(inflater.getContext()).load(n.getImgsrc()).into(holder.img);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(inflater.getContext(), New_one.class);
                intent.putExtra("news",n.getUrl_3w());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //context.startActivity(intent);
                inflater.getContext().startActivity(intent);
            }
        });
        return convertView;
    }
}
