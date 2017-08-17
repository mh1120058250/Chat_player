package com.amap.map3d.demo.chat_player.Adpter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.amap.map3d.demo.chat_player.R;
import com.amap.map3d.demo.chat_player.bean.Friend;
import com.amap.map3d.demo.chat_player.bean.User;

import java.util.List;

/**
 * 2017/7/26.
 */

public class ContactAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private List<Friend> list;
    public ContactAdapter(Context context, List<Friend> list)
    {
        inflater = LayoutInflater.from(context);
        this.list = list;
    }
    @Override
    public int getCount() {
        Log.e("adapter-------->",list.size()+"");
        return list.size();
    }

    @Override
    public Friend getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    ViewHolder holder = null;
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
        {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_contact,viewGroup,false);
            holder.ivAvatar = (ImageView) view.findViewById(R.id.iv_recent_avatar);
            holder.tvName = (TextView) view.findViewById(R.id.tv_recent_name);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        Friend friend = list.get(i);
        User user =friend.getFriendUser();
        //好友头像
        Object obj = user.getAvatar();
        if(obj instanceof String){
            String avatar=(String)obj;
            holder.ivAvatar.setImageResource(R.mipmap.ic_launcher);
        }else{
//            int defaultRes = (int)obj;
//            holder.ivAvatar.setImageResource(defaultRes);
        }
        //好友名称
        holder.tvName.setText(user==null?"未知":user.getUsername());
        return view;
    }
    class ViewHolder{
        private ImageView ivAvatar;
        private TextView tvName;
    }
}
