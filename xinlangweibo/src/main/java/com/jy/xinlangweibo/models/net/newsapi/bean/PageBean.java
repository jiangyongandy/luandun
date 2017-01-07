package com.jy.xinlangweibo.models.net.newsapi.bean;

import com.jy.xinlangweibo.models.net.BaseBean;

import java.util.List;

/**
 * Created by JIANG on 2017/1/4.
 */

public class PageBean extends BaseBean {

    public int allPages;
    public int currentPage;
    public int allNum;
    public int maxResult;
    public List<ContentlistBean> contentlist;

}
