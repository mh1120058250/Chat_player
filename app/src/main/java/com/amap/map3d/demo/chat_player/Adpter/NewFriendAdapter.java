package com.amap.map3d.demo.chat_player.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.map3d.demo.chat_player.Config;
import com.amap.map3d.demo.chat_player.R;
import com.amap.map3d.demo.chat_player.bean.AgreeAddFriendMessage;
import com.amap.map3d.demo.chat_player.bean.User;
import com.amap.map3d.demo.chat_player.db.NewFriend;
import com.amap.map3d.demo.chat_player.db.NewFriendManager;
import com.amap.map3d.demo.chat_player.model.UserModel;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 2017/7/25.
 */

public class NewFriendAdapter extends BaseAdapter{
    private List<NewFriend> list;
    private LayoutInflater inflater;
    private Context context;
    public NewFriendAdapter(Context context,List<NewFriend> list){
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public NewFriend getItem(int i) {
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
            view = inflater.inflate(R.layout.item_new_friend,viewGroup,false);
            holder.btnAggree = (Button) view.findViewById(R.id.btn_aggree);
            holder.ivAvatar = (ImageView) view.findViewById(R.id.iv_recent_avatar);
            holder.tvMsg = (TextView) view.findViewById(R.id.tv_recent_msg);
            holder.tvName = (TextView) view.findViewById(R.id.tv_recent_name);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        final NewFriend newFriend = list.get(i);
        holder.ivAvatar.setImageResource(R.mipmap.ic_launcher);
        holder.tvName.setText(newFriend == null ? "未知" : newFriend.getName());
        holder.tvMsg.setText(newFriend == null ? "未知" : newFriend.getMsg());
        Integer status = newFriend.getStatus();
        if (status == null || status == Config.STATUS_VERIFY_NONE || status == Config.STATUS_VERIFY_READED) {//未添加/已读未添加
            holder.btnAggree.setText("接受");
            holder.btnAggree.setEnabled(true);
            holder.btnAggree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {//发送消息
                    agreeAdd(newFriend, new SaveListener<Object>() {
                        @Override
                        public void done(Object o, BmobException e) {
                            if (e == null) {
                                holder.btnAggree.setText("已添加");
                                holder.btnAggree.setEnabled(false);
                                notifyDataSetChanged();
                            } else {
                                holder.btnAggree.setEnabled(true);
                                Logger.e("添加好友失败:" + e.getMessage());
                                toast("添加好友失败:" + e.getMessage());
                            }
                        }
                    });
                }
            });
        }
        else {
            holder.btnAggree.setText("已添加");
            holder.btnAggree.setEnabled(false);
        }
        return view;
    }

    private void toast(String str) {
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }

    class ViewHolder{
        private ImageView ivAvatar;
        private TextView tvName,tvMsg;
        private Button btnAggree;
    }
    /**
     * 添加到好友表中...
     *
     * @param add
     * @param listener
     */
    private void agreeAdd(final NewFriend add, final SaveListener<Object> listener) {
        User user = new User();
        user.setObjectId(add.getUid());
        UserModel.getInstance()
                .agreeAddFriend(user, new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            sendAgreeAddFriendMessage(add, listener);
                        } else {
                            Logger.e(e.getMessage());
                            listener.done(null, e);
                        }
                    }
                });
    }

    /**
     * 发送同意添加好友的请求
     */
    private void sendAgreeAddFriendMessage(final NewFriend add, final SaveListener<Object> listener) {
        BmobIMUserInfo info = new BmobIMUserInfo(add.getUid(), add.getName(), add.getAvatar());
        //如果为true,则表明为暂态会话，也就是说该会话仅执行发送消息的操作，不会保存会话和消息到本地数据库中
        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, true, null);
        //这个obtain方法才是真正创建一个管理消息发送的会话
        BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
        //而AgreeAddFriendMessage的isTransient设置为false，表明我希望在对方的会话数据库中保存该类型的消息
        AgreeAddFriendMessage msg = new AgreeAddFriendMessage();
        final User currentUser = BmobUser.getCurrentUser(User.class);
        msg.setContent("我通过了你的好友验证请求，我们可以开始 聊天了!");//---这句话是直接存储到对方的消息表中的
        Map<String, Object> map = new HashMap<>();
        map.put("msg", currentUser.getUsername() + "同意添加你为好友");//显示在通知栏上面的内容
        map.put("uid", add.getUid());//发送者的uid-方便请求添加的发送方找到该条添加好友的请求
        map.put("time", add.getTime());//添加好友的请求时间
        msg.setExtraMap(map);
        conversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                if (e == null) {//发送成功
                    //修改本地的好友请求记录
                    NewFriendManager.getInstance(context).updateNewFriend(add, Config.STATUS_VERIFIED);
                    listener.done(msg, e);
                } else {//发送失败
                    Logger.e(e.getMessage());
                    listener.done(msg, e);
                }
            }
        });
    }
}
