package com.jy.xinlangweibo.models.net.newsapi.bean;

import com.jy.xinlangweibo.models.net.BaseBean;

import java.util.List;

/**
 * Created by JIANG on 2017/1/4.
 */

public class NewsChannelList extends BaseBean {

    public int showapi_res_code;
    public String showapi_res_error;
    public ShowapiResBodyBean showapi_res_body;

    public static class ShowapiResBodyBean {
        public int totalNum;
        public int ret_code;
        public List<ChannelListBean> channelList;

    }
}
