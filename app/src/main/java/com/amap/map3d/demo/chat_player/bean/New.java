package com.amap.map3d.demo.chat_player.bean;

/**
 * Created by ZhangYang on 2017/6/20.
 */

public class New {
    private String title,url_3w,imgsrc,source,replyCount;
    private int hasHead,hasImg,imgType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        New aNew = (New) o;

        if (hasHead != aNew.hasHead) return false;
        if (hasImg != aNew.hasImg) return false;
        if (imgType != aNew.imgType) return false;
        if (title != null ? !title.equals(aNew.title) : aNew.title != null) return false;
        if (url_3w != null ? !url_3w.equals(aNew.url_3w) : aNew.url_3w != null) return false;
        if (imgsrc != null ? !imgsrc.equals(aNew.imgsrc) : aNew.imgsrc != null) return false;
        if (source != null ? !source.equals(aNew.source) : aNew.source != null) return false;
        return replyCount != null ? replyCount.equals(aNew.replyCount) : aNew.replyCount == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (url_3w != null ? url_3w.hashCode() : 0);
        result = 31 * result + (imgsrc != null ? imgsrc.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (replyCount != null ? replyCount.hashCode() : 0);
        result = 31 * result + hasHead;
        result = 31 * result + hasImg;
        result = 31 * result + imgType;
        return result;
    }

    @Override
    public String toString() {
        return "New{" +
                "title='" + title + '\'' +
                ", url_3w='" + url_3w + '\'' +
                ", imgsrc='" + imgsrc + '\'' +
                ", source='" + source + '\'' +
                ", replyCount='" + replyCount + '\'' +
                ", hasHead=" + hasHead +
                ", hasImg=" + hasImg +
                ", imgType=" + imgType +
                '}';
    }

    public New(String title, String url_3w, String imgsrc, String source) {
        this.title = title;
        this.url_3w = url_3w;
        this.imgsrc = imgsrc;
        this.source = source;
    }

    public New(String title, String url_3w, String imgsrc, String source, String replyCount, int hasHead, int hasImg, int imgType) {
        this.title = title;
        this.url_3w = url_3w;
        this.imgsrc = imgsrc;
        this.source = source;
        this.replyCount = replyCount;
        this.hasHead = hasHead;
        this.hasImg = hasImg;
        this.imgType = imgType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl_3w() {
        return url_3w;
    }

    public void setUrl_3w(String url_3w) {
        this.url_3w = url_3w;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(String replyCount) {
        this.replyCount = replyCount;
    }

    public int getHasHead() {
        return hasHead;
    }

    public void setHasHead(int hasHead) {
        this.hasHead = hasHead;
    }

    public int getHasImg() {
        return hasImg;
    }

    public void setHasImg(int hasImg) {
        this.hasImg = hasImg;
    }

    public int getImgType() {
        return imgType;
    }

    public void setImgType(int imgType) {
        this.imgType = imgType;
    }
}
