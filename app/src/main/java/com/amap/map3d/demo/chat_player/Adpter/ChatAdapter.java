package com.amap.map3d.demo.chat_player.Adpter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.map3d.demo.chat_player.R;
import com.amap.map3d.demo.chat_player.activity.ChatActivity;
import com.amap.map3d.demo.chat_player.activity.Map_one;
import com.amap.map3d.demo.chat_player.application.BmobIMApplication;
import com.amap.map3d.demo.chat_player.bean.ChatMessage;
import com.amap.map3d.demo.chat_player.bean.Recorder;
import com.amap.map3d.demo.chat_player.bean.Weizhi;
import com.amap.map3d.demo.chat_player.view.MediaManager;
import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMLocationMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.v3.BmobUser;

/**
 *  on 2017/7/29.
 */

public class ChatAdapter extends BaseAdapter {
    private List<BmobIMMessage> list;
    private Context context;
    BmobIMApplication app;
    private LayoutInflater inflater;
    public ChatAdapter(List<BmobIMMessage> list, Context context) {
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }
    public void addMessage(BmobIMMessage message) {
        list.addAll(Arrays.asList(message));
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setList(BmobIMMessage list) {
        this.list.add(list);
        notifyDataSetChanged();
    }
    class ViewHolder{
        private ImageView ivAvatar,img;
        private TextView tvName,tvMsg,tvtime;
        private Button btnAggree;
        TextView seconds;// 时间
        View length;// 对话框长度
        View viewanim;
    }
    ViewHolder holder =null;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("BBB3", "getView: " + list.get(position).getMsgType());
        app= (BmobIMApplication) inflater.getContext().getApplicationContext();
        if(list.get(position).getMsgType().equals(BmobIMMessageType.LOCATION.getType())){
            BmobIMMessage msgs= list.get(position);
            final  BmobIMLocationMessage msg = BmobIMLocationMessage.buildFromDB(msgs);
            Log.e("BBB4", "getView: " + msg.getAddress());
            if (list.get(position).getFromId().equals(BmobUser.getCurrentUser().getObjectId())) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_dili, parent, false);
                holder.img= convertView.findViewById(R.id.img);
                holder.tvtime=convertView.findViewById(R.id.id_form_msg_date);
                holder.tvMsg=convertView.findViewById(R.id.weizhi);
                convertView.setTag(holder);
                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent1=new Intent(inflater.getContext(), Map_one.class);
                        intent1.putExtra("x",msg.getLatitude());
                        intent1.putExtra("y",msg.getLongitude());
                        inflater.getContext().startActivity(intent1);
                    }
                });
                holder.tvMsg.setText(msg.getAddress());
                return convertView;
            } else {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_dili1, parent, false);
                holder.img= convertView.findViewById(R.id.img);
                holder.tvtime=convertView.findViewById(R.id.id_form_msg_date);
                holder.tvMsg=convertView.findViewById(R.id.weizhi);
                convertView.setTag(holder);
                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent1=new Intent(inflater.getContext(), Map_one.class);
                        intent1.putExtra("x",msg.getLatitude());
                        intent1.putExtra("y",msg.getLongitude());
                        inflater.getContext().startActivity(intent1);
                    }
                });
                holder.tvMsg.setText(msg.getAddress());
                return convertView;
            }
        }
        if(list.get(position).getMsgType().equals(BmobIMMessageType.VOICE.getType())){
            BmobIMMessage msgs= list.get(position);
           final BmobIMAudioMessage msg = BmobIMAudioMessage.buildFromDB(true,msgs);
            if (list.get(position).getFromId().equals(BmobUser.getCurrentUser().getObjectId())) {
                // 获取系统宽度
                WindowManager wManager = (WindowManager) context
                        .getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics outMetrics = new DisplayMetrics();
                wManager.getDefaultDisplay().getMetrics(outMetrics);
                int  mMaxItemWith = (int) (outMetrics.widthPixels * 0.7f);
                int  mMinItemWith = (int) (outMetrics.widthPixels * 0.15f);
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_layout, parent, false);
                holder.seconds=(TextView) convertView.findViewById(R.id.recorder_time);
                holder.tvtime=convertView.findViewById(R.id.id_form_msg_date);
                holder.length=convertView.findViewById(R.id.recorder_length);
                holder.viewanim = convertView.findViewById(R.id.id_recorder_anim);
                convertView.setTag(holder);
                double s=msg.getDuration();
                holder.seconds.setText(s+"\"");
                ViewGroup.LayoutParams lParams=holder.length.getLayoutParams();
                lParams.width=(int) (mMinItemWith+mMaxItemWith/60f*s);
                holder.length.setLayoutParams(lParams);
                return convertView;
            } else {
                // 获取系统宽度
                WindowManager wManager = (WindowManager) context
                        .getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics outMetrics = new DisplayMetrics();
                wManager.getDefaultDisplay().getMetrics(outMetrics);
                int  mMaxItemWith = (int) (outMetrics.widthPixels * 0.7f);
                int  mMinItemWith = (int) (outMetrics.widthPixels * 0.15f);
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_layout1, parent, false);
                holder.seconds=(TextView) convertView.findViewById(R.id.recorder_time);
                holder.tvtime=convertView.findViewById(R.id.id_form_msg_date);
                holder.length=convertView.findViewById(R.id.recorder_length);
                convertView.setTag(holder);
                Log.e("BBB1", "getView: " + msg.getRemoteUrl());
                double s=msg.getDuration();
                holder.seconds.setText(s+"\"");
                ViewGroup.LayoutParams lParams=holder.length.getLayoutParams();
                lParams.width=(int) (mMinItemWith+mMaxItemWith/60f*s);
                holder.length.setLayoutParams(lParams);
                return convertView;
            }
        }
        if (list.get(position).getMsgType().equals("image")) {
            BmobIMMessage msgs= list.get(position);
            BmobIMImageMessage msg = BmobIMImageMessage.buildFromDB(false,msgs);
            if (list.get(position).getFromId().equals(BmobUser.getCurrentUser().getObjectId())) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_image, parent, false);
                holder.img = (ImageView) convertView.findViewById(R.id.img);
                holder.tvtime=convertView.findViewById(R.id.id_form_msg_date);
                convertView.setTag(holder);
                Log.e("BBB1", "getView: " + msg.getRemoteUrl());
                String path=msg.getRemoteUrl().split("&")[0];
                Glide.with(inflater.getContext()).load(path).into(holder.img);
                return convertView;
            } else {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_image1, parent, false);
                holder.img = (ImageView) convertView.findViewById(R.id.img);
                holder.tvtime=convertView.findViewById(R.id.id_form_msg_date);
                convertView.setTag(holder);
                Log.e("BBB2", "getView: "+msg.getRemoteUrl());
                Glide.with(inflater.getContext()).load(msg.getRemoteUrl()).into(holder.img);
                return convertView;
            }
        }
        BmobIMMessage msg = list.get(position);
        Log.e("BBB", "getView: " + msg.getMsgType());
        if (!list.get(position).getFromId().equals(BmobUser.getCurrentUser().getObjectId())) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_from_msg, parent, false);
            holder.tvtime=convertView.findViewById(R.id.id_form_msg_date);
            holder.tvMsg = (TextView) convertView.findViewById(R.id.id_from_msg_info);
            convertView.setTag(holder);
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            holder.tvtime.setText(df.format(msg.getCreateTime()));
            holder.tvMsg.setText(list.get(position).getContent());
            return convertView;
        } else {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_to_msg, parent, false);
            holder.tvtime=convertView.findViewById(R.id.id_form_msg_date);
            holder.tvMsg = (TextView) convertView.findViewById(R.id.id_to_msg_info);
            convertView.setTag(holder);
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Log.e("BBB7", "getView: "+df.format(msg.getCreateTime()));
//            holder.tvtime.setText(df.format(msg.getCreateTime()));
            holder.tvMsg.setText(list.get(position).getContent());
            return convertView;

        }
    }
}
