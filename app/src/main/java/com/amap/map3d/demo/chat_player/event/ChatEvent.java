package com.amap.map3d.demo.chat_player.event;

import cn.bmob.newim.bean.BmobIMUserInfo;

public class ChatEvent {

    public BmobIMUserInfo info;

    public ChatEvent(BmobIMUserInfo info){
        this.info=info;
    }
}
