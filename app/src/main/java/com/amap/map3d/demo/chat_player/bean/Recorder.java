package com.amap.map3d.demo.chat_player.bean;

/**
 * Created by Administrator on 2017/7/24 0024.
 */

public class Recorder {
   private float time;

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getFilePathString() {
        return filePathString;
    }

    public void setFilePathString(String filePathString) {
        this.filePathString = filePathString;
    }

    private String filePathString;

    public Recorder(float time, String filePathString) {
        super();
        this.time = time;
        this.filePathString = filePathString;
    }
}
