package com.amap.map3d.demo.chat_player.Adpter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.map3d.demo.chat_player.R;
import com.amap.map3d.demo.chat_player.activity.Image_one;
import com.amap.map3d.demo.chat_player.activity.Map_one;
import com.amap.map3d.demo.chat_player.activity.Talking;
import com.amap.map3d.demo.chat_player.application.BmobIMApplication;
import com.amap.map3d.demo.chat_player.bean.ChatMessage;
import com.amap.map3d.demo.chat_player.bean.Recorder;
import com.amap.map3d.demo.chat_player.bean.Weizhi;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import static android.R.attr.bitmap;

/**
 * 2017/7/14.
 */

public class ChatMessageAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private List mDatas;
    private BmobIMApplication app;
    private int mMinItemWith;// 设置对话框的最大宽度和最小宽度
    private int mMaxItemWith;
    public ChatMessageAdapter(List mDatas,Context context ) {
        inflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i)
    {
            return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        if(mDatas.get(position) instanceof ChatMessage) {
            ChatMessage chatMessage = (ChatMessage) mDatas.get(position);
            //判断消息类型
            if (chatMessage.getType() == ChatMessage.Type.INCOMING) {
                return 0;
            }else{
                return 1;
            }
        }else if(mDatas.get(position) instanceof Recorder) {
            return 2;
        }else if(mDatas.get(position) instanceof Weizhi){
           return 4;
        }else{
            return 3;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        app= (BmobIMApplication) inflater.getContext().getApplicationContext();
            //通过ItemType设置不同的布局
            if (getItemViewType(i) == 0)
            {
                ChatMessage chatMessage = (ChatMessage) mDatas.get(i);
                ViewHolder viewHolder = null;
                if (view == null)
                {
                    view = inflater.inflate(R.layout.item_from_msg,viewGroup,false);
                    viewHolder = new ViewHolder();
                    viewHolder.mDate = (TextView) view.findViewById(R.id.id_form_msg_date);
                    viewHolder.mMsg = (TextView) view.findViewById(R.id.id_from_msg_info);
                    view.setTag(viewHolder);
                } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            //设置数据
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            viewHolder.mDate.setText(df.format(chatMessage.getDate()));
            viewHolder.mMsg.setText(chatMessage.getMsg());
            return view;
            }else if (getItemViewType(i) == 1){
                ChatMessage chatMessage = (ChatMessage) mDatas.get(i);
                ViewHolder viewHolder = null;
                if (view == null)
                {
                    view = inflater.inflate(R.layout.item_to_msg,viewGroup,false);
                    viewHolder = new ViewHolder();
                    viewHolder.mDate = (TextView) view.findViewById(R.id.id_to_msg_date);
                    viewHolder.mMsg = (TextView) view.findViewById(R.id.id_to_msg_info);
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) view.getTag();
                }
                //设置数据
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                viewHolder.mDate.setText(df.format(chatMessage.getDate()));
                viewHolder.mMsg.setText(chatMessage.getMsg());
                return view;
            }else if(getItemViewType(i)==2){
                // 获取系统宽度
                WindowManager wManager = (WindowManager)inflater.getContext()
                        .getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics outMetrics = new DisplayMetrics();
                wManager.getDefaultDisplay().getMetrics(outMetrics);
                mMaxItemWith = (int) (outMetrics.widthPixels * 0.7f);
                mMinItemWith = (int) (outMetrics.widthPixels * 0.15f);
                ViewHolder viewHolder = null;
                Recorder recorder= (Recorder) mDatas.get(i);
                if (view == null)
                {
                    view = inflater.inflate(R.layout.item_layout, viewGroup, false);
                    viewHolder=new ViewHolder();
                    viewHolder.seconds=(TextView) view.findViewById(R.id.recorder_time);
                    viewHolder.length=view.findViewById(R.id.recorder_length);
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) view.getTag();
                }
                //设置数据
                viewHolder.seconds.setText(Math.round(recorder.getTime())+"\"");
                ViewGroup.LayoutParams lParams=viewHolder.length.getLayoutParams();
                lParams.width=(int) (mMinItemWith+mMaxItemWith/60f*recorder.getTime());
                viewHolder.length.setLayoutParams(lParams);
                return view;
            }else if(getItemViewType(i)==4){
                final Weizhi weizhi = (Weizhi) mDatas.get(i);
                ViewHolder viewHolder = null;
                if (view == null)
                {
                    view = inflater.inflate(R.layout.item_dili,viewGroup,false);
                    viewHolder = new ViewHolder();
                    viewHolder.img= (ImageView) view.findViewById(R.id.img);
                    viewHolder.wei = (TextView) view.findViewById(R.id.weizhi);
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) view.getTag();
                }
                Log.e("zzz",weizhi.getBuff());
                viewHolder.wei.setText(weizhi.getBuff());
                viewHolder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent1=new Intent(inflater.getContext(), Map_one.class);
                        intent1.putExtra("x",weizhi.getJindu());
                        intent1.putExtra("y",weizhi.getWeidu());
                        inflater.getContext().startActivity(intent1);
                    }
                });
                return view;
            }else {
                final Bitmap bm= (Bitmap) mDatas.get(i);
                ViewHolder viewHolder = null;
                if (view == null)
                {
                    view = inflater.inflate(R.layout.item_image,viewGroup,false);
                    viewHolder = new ViewHolder();
                    viewHolder.img = (ImageView) view.findViewById(R.id.img);
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) view.getTag();
                }
                //设置数据
                viewHolder.img.setImageBitmap(bm);
                viewHolder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(inflater.getContext(),Image_one.class);
                        ByteArrayOutputStream baos=new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte [] bitmapByte =baos.toByteArray();
                        intent.putExtra("bitmap", bitmapByte);
                        inflater.getContext().startActivity(intent);
                    }
                });
                return view;
            }

    }
    private class ViewHolder{
        private TextView mDate,mMsg;
        TextView seconds;// 时间
        View length;// 对话框长度
        ImageView img;
        TextView wei;
    }
}
