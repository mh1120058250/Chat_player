package com.amap.map3d.demo.chat_player.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.amap.map3d.demo.chat_player.R;
import com.amap.map3d.demo.chat_player.bean.Conversation;
import com.amap.map3d.demo.chat_player.util.TimeUtil;

import java.util.List;

/**
 *2017/7/24.
 */

public class ConversationFragmentAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private List<Conversation> list;
    private Context context;
    public ConversationFragmentAdapter(Context context,List<Conversation> list)
    {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Conversation getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    ViewHolder holder = null;
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view==null)
        {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_conversation,viewGroup,false);
            holder.ivAvatar = (ImageView) view.findViewById(R.id.iv_recent_avatar);
            holder.tvMsg = (TextView) view.findViewById(R.id.tv_recent_msg);
            holder.tvName = (TextView) view.findViewById(R.id.tv_recent_name);
            holder.tvTime = (TextView) view.findViewById(R.id.tv_recent_time);
            holder.tvUnread = (TextView) view.findViewById(R.id.tv_recent_unread);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        final Conversation conversation =list.get(i);
        holder.tvMsg.setText(conversation.getLastMessageContent());
        holder.tvName.setText(conversation.getcName());
        //Toast.makeText(context,conversation.getLastMessageContent(), Toast.LENGTH_SHORT).show();
        //查询指定未读消息数
        long unread = conversation.getUnReadCount();
        if(unread>0){
            holder.tvUnread.setVisibility(View.VISIBLE);
            holder.tvUnread.setText(String.valueOf(unread));
        }else{
            holder.tvUnread.setVisibility(View.GONE);
        }
        holder.tvTime.setText(TimeUtil.getChatTime(false,conversation.getLastMessageTime()));
        //会话图标
        Object obj = conversation.getAvatar();
        if(obj instanceof String){
            String avatar=(String)obj;
            holder.ivAvatar.setImageResource(R.mipmap.ic_launcher);
        }else{
            int defaultRes = (int)obj;
            holder.ivAvatar.setImageResource(defaultRes);
        }
        return view;
    }
    class ViewHolder{
        private ImageView ivAvatar;
        private TextView tvName,tvMsg,tvTime,tvUnread;
    }
}
