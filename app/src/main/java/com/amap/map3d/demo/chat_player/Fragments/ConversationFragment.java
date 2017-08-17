package com.amap.map3d.demo.chat_player.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListView;


import com.amap.map3d.demo.chat_player.Adpter.ConversationFragmentAdapter;
import com.amap.map3d.demo.chat_player.R;
import com.amap.map3d.demo.chat_player.bean.Conversation;
import com.amap.map3d.demo.chat_player.bean.NewFriendConversation;
import com.amap.map3d.demo.chat_player.bean.PrivateConversation;
import com.amap.map3d.demo.chat_player.db.NewFriend;
import com.amap.map3d.demo.chat_player.db.NewFriendManager;
import com.amap.map3d.demo.chat_player.event.RefreshEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.MessageListHandler;

/**会话界面

 */
public class ConversationFragment extends Fragment implements MessageListHandler {
    private SwipeRefreshLayout conversationSrl;
    private ListView conversationLv;
    private ConversationFragmentAdapter adapter;
    private List<Conversation> conversationList;
    private View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_conversation,container,false);
        conversationList = getConversations();
        conversationSrl = (SwipeRefreshLayout) rootView.findViewById(R.id.id_sw_refresh);
        conversationLv = (ListView) rootView.findViewById(R.id.id_conversion_listView);
        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        query();
    }
    @Override
    public void onPause() {
        super.onPause();
        BmobIM.getInstance().removeMessageListHandler(this);
    }
    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Override
    public void onResume() {
        super.onResume();
        conversationSrl.setRefreshing(true);
        BmobIM.getInstance().addMessageListHandler(this);
        query();
    }
    private void setListener() {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                conversationSrl.setRefreshing(true);
                query();
            }
        });
        conversationSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initListener();
        initDatas();
    }
    private void initDatas() {
        adapter = new ConversationFragmentAdapter(getActivity(),conversationList);
        conversationLv.setAdapter(adapter);
    }
    private void initListener() {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                conversationSrl.setRefreshing(true);
                query();
            }
        });
        conversationSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        conversationLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.getItem(i).onClick(getActivity());
            }
        });

    }
    /**
     查询本地会话
     */
    public void query(){
        conversationList = getConversations();
        adapter.notifyDataSetChanged();
        conversationSrl.setRefreshing(false);
    }
//    /**
//     * 查询本地会话
//     */
//    public void query() {
//        UserModel.getInstance().queryFriends(
//
//                new FindListener<Friend>() {
//                    @Override
//                    public void done(List<Friend> list, BmobException e) {
//
//                        if (e == null) {
//                            List<Friend> friends = new ArrayList<Friend>();
//                            friends.clear();
//                            //添加首字母
//                            for (int i = 0; i < list.size(); i++) {
//                                Friend friend = list.get(i);
//                                String username = friend.getFriendUser().getUsername();
//                                String pinyin = Pinyin.toPinyin(username.charAt(0));
////                    Logger.i(pinyin);
//                                friend.setPinyin(pinyin.substring(0, 1).toUpperCase());
//                                friends.add(friend);
//                            }
//                            conversationList = getConversations();
//                            adapter.notifyDataSetChanged();
//                            conversationSrl.setRefreshing(false);
//                        } else {
//
//                            conversationList = getConversations();
//                            adapter.notifyDataSetChanged();
//                            conversationSrl.setRefreshing(false);
//                            Logger.i(e.getMessage() + "--" + e.getErrorCode());
//                        }
//                    }
//                }
//
//
//        );
//    }
    /**
     * 获取会话列表的数据：增加新朋友会话
     * @return
     */
    private List<Conversation> getConversations(){
        //添加会话
        List<Conversation> conversationList = new ArrayList<>();
        conversationList.clear();
        List<BmobIMConversation> list = BmobIM.getInstance().loadAllConversation();
        if(list!=null && list.size()>0){
            for (BmobIMConversation item:list){
                switch (item.getConversationType()){
                    case 1://私聊
                        conversationList.add(new PrivateConversation(item));
                        break;
                    default:
                        break;
                }
            }
        }
        //添加新朋友会话-获取好友请求表中最新一条记录
        List<NewFriend> friends = NewFriendManager.getInstance(getActivity()).getAllNewFriend();
        if(friends!=null && friends.size()>0){
            conversationList.add(new NewFriendConversation(friends.get(0)));
        }
        //重新排序
        Collections.sort(conversationList);
        return conversationList;
    }
    /**注册自定义消息接收事件
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event){
        log("---会话页接收到自定义消息---");
        //因为新增`新朋友`这种会话类型
        conversationList = getConversations();
        adapter.notifyDataSetChanged();
    }

    private void log(String s) {
        Log.e("----------------",s);
    }

    /**注册离线消息接收事件
     * @param event
     */
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event){
        //重新刷新列表
        conversationList = getConversations();
        adapter.notifyDataSetChanged();
    }

    /**注册消息接收事件
     * @param event
     * 1、与用户相关的由开发者自己维护，SDK内部只存储用户信息
     * 2、开发者获取到信息后，可调用SDK内部提供的方法更新会话
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event){
        //重新获取本地消息并刷新列表
        conversationList = getConversations();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        Log.e("eee", "onMessageReceive: "+list.toString() );
        initDatas();
    }
}
