package com.amap.map3d.demo.chat_player.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.map3d.demo.chat_player.R;
import com.amap.map3d.demo.chat_player.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 *  2017/7/19.
 */

public class ActivitySearchAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private List<User> users = new ArrayList<>();
    public ActivitySearchAdapter(Context context)
    {
        inflater = LayoutInflater.from(context);
    }
    public void setDatas(List<User> list) {
        users.clear();
        if (null != list) {
            users.addAll(list);
        }
    }
    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view==null)
        {
            view = inflater.inflate(R.layout.item,viewGroup,false);

            holder = new ViewHolder();
            //holder.iconImg = view.findViewById(R.id.avatar);
            holder.nameText = (TextView) view.findViewById(R.id.tv);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        User user = users.get(i);
        holder.nameText.setText(user.getUsername());
        //holder.iconImg.setImageResource(R.mipmap.ic_launcher);
        return view;
    }
    class ViewHolder{
        //private ImageView iconImg;
        private TextView nameText;
    }
}
