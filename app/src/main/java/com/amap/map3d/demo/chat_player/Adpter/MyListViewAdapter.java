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
 * 2017/6/21.
 */

public class MyListViewAdapter extends BaseAdapter {
    //item的类型
    private final int VIEWTYPE_TOP = 0;
    private final int VIEWTYPE_CONTENT = 1;
    private final int VIEWTYPE_MID = 2;
    private final int VIEWTYPE_COUNT = 3;
    //新闻集合
    private List<New> news;
    private LayoutInflater inflater;
    //private BitmapCache bitmapCache;
    public MyListViewAdapter(List<New> news, Context context) {
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

    @Override
    public int getViewTypeCount() {
        return VIEWTYPE_COUNT;
    }
    //设置item类型
    @Override
    public int getItemViewType(int position) {
        New n  = news.get(position);
        if (n.getHasHead()==1 && n.getHasImg() == 1)
        {
            return VIEWTYPE_TOP;
        }
        if (n.getImgType() == 1)
        {
            return  VIEWTYPE_MID;
        }
        return VIEWTYPE_CONTENT;
    }

    class ViewHolder{
        private ImageView img;
        private TextView title,content,time;
    }
    ViewHolder holder = null;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null)
        {
            holder = new ViewHolder();
            //获取item的类型
            switch (getItemViewType(position))
            {
                case VIEWTYPE_CONTENT:
                    convertView = inflater.inflate(R.layout.item1,parent,false);
                    holder.title = (TextView) convertView.findViewById(R.id.title);
                    holder.content = (TextView) convertView.findViewById(R.id.content);
                    holder.time = (TextView) convertView.findViewById(R.id.time);
                    holder.img = (ImageView) convertView.findViewById(R.id.icon);
                    convertView.setTag(holder);
                    break;
                case VIEWTYPE_TOP:
                    convertView = inflater.inflate(R.layout.item_big,parent,false);
                    holder.title = (TextView) convertView.findViewById(R.id.title_big);
                    holder.content = (TextView) convertView.findViewById(R.id.resourse_big);
                    holder.time = (TextView) convertView.findViewById(R.id.count_big);
                    holder.img = (ImageView) convertView.findViewById(R.id.image_big);
                    convertView.setTag(holder);
                    break;
                case VIEWTYPE_MID:
                    convertView = inflater.inflate(R.layout.item_mid,parent,false);
                    holder.title = (TextView) convertView.findViewById(R.id.title_mid);
                    holder.content = (TextView) convertView.findViewById(R.id.resourse_mid);
                    holder.time = (TextView) convertView.findViewById(R.id.count_mid);
                    holder.img = (ImageView) convertView.findViewById(R.id.image_mid);
                    convertView.setTag(holder);
                    break;
            }
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }
       final New n = news.get(position);
        holder.title.setText(n.getTitle());
        holder.content.setText(n.getSource());
        holder.time.setText(getNumber(n.getReplyCount()));
        Glide.with(inflater.getContext()).load(n.getImgsrc()).into(holder.img);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(inflater.getContext(),New_one.class);
                intent.putExtra("title",n.getTitle());
                intent.putExtra("news",n.getUrl_3w());
                intent.putExtra("img",n.getImgsrc());
                intent.putExtra("content",n.getSource());
                inflater.getContext().startActivity(intent);
            }
        });
        return convertView;
    }
    /**
     * 换算跟帖数字
     */
    private String getNumber(String str)
    {
        int i = 0;
        if (str == null)
        {

        }else
        {
            i = Integer.parseInt(str);
            if (i>9999)
            {
                float f = i/10000;
                return  f+"万跟帖";
            }
        }
        return i+"跟帖";
    }
}
