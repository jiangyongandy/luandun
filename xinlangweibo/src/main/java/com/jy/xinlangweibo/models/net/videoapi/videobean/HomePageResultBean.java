package com.jy.xinlangweibo.models.net.videoapi.videobean;

import com.jy.xinlangweibo.models.net.BaseBean;

import java.util.List;

/**
 * Created by JIANG on 2016/12/5.
 */

public class HomePageResultBean extends BaseBean{

    public String msg;
    public RetBean ret;
    public String code;

    public static class RetBean {
        public List<ListBean> list;

    }
}
