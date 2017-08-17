package com.amap.map3d.demo.chat_player.bean;

/**
 * Created by Administrator on 2017/7/26 0026.
 */

public class Weizhi {
    private String buff;
    private double jindu;
    private double weidu;

    public Weizhi(String buff, double jindu, double weidu) {
        this.buff = buff;
        this.jindu = jindu;
        this.weidu = weidu;
    }

    public String getBuff() {
        return buff;
    }

    public void setBuff(String buff) {
        this.buff = buff;
    }

    public double getJindu() {
        return jindu;
    }

    public void setJindu(double jindu) {
        this.jindu = jindu;
    }

    public double getWeidu() {
        return weidu;
    }

    public void setWeidu(double weidu) {
        this.weidu = weidu;
    }
}
