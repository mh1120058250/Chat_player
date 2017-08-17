package com.amap.map3d.demo.chat_player.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.amap.map3d.demo.chat_player.R;
import com.amap.map3d.demo.chat_player.bean.ChatMessage;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14 0014.
 */

public class Myadout extends BaseAdapter {
    private LayoutInflater inflater;
    private List<ChatMessage> mDatas;

    public Myadout(Context context , List<ChatMessage> mDatas) {
        inflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = mDatas.get(position);
        //判断消息类型
        if (chatMessage.getType() == ChatMessage.Type.INCOMING)
        {
            return 0;
        }
        return 1;
    }
    @Override
    public int getViewTypeCount() {
        return 2;
    }
    private class ViewHolder{
        private TextView mDate,mMsg;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ChatMessage chatMessage=new ChatMessage();
        ViewHolder holder=null;
        if (view == null)
        {
            //通过ItemType设置不同的布局
            if (getItemViewType(i) == 0)
            {
                view = inflater.inflate(R.layout.item_from_msg,viewGroup,false);
                holder = new ViewHolder();
                holder.mDate = (TextView) view.findViewById(R.id.id_form_msg_date);
                holder.mMsg = (TextView) view.findViewById(R.id.id_from_msg_info);
            }else {
                view = inflater.inflate(R.layout.item_to_msg,viewGroup,false);
                holder = new ViewHolder();
                holder.mDate = (TextView) view.findViewById(R.id.id_to_msg_date);
                holder.mMsg = (TextView) view.findViewById(R.id.id_to_msg_info);
            }
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        return view;
    }
}
