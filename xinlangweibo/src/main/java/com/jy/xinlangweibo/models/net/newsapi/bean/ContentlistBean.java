package com.jy.xinlangweibo.models.net.newsapi.bean;

import com.jy.xinlangweibo.models.net.BaseBean;

import java.util.List;

/**
 * Created by JIANG on 2017/1/4.
 */

public class ContentlistBean extends BaseBean {

    public String pubDate;
    public boolean havePic;
    public String title;
    public String channelName;
    public String desc;
    public String source;
    public String channelId;
    public String link;
    public List<Object> allList;
    public List<ImageBean> imageurls;

    public static class ImageBean extends BaseBean {
        public int height;
        public int width;
        public String url;
    }
}
