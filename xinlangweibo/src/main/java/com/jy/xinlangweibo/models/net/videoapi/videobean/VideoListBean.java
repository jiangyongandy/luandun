package com.jy.xinlangweibo.models.net.videoapi.videobean;

import com.jy.xinlangweibo.models.net.BaseBean;

import java.util.List;

/**
 * Created by JIANG on 2016/12/23.
 */

public class VideoListBean extends BaseBean {

    public String msg;
    public RetBean ret;
    public String code;

    public static class RetBean {
        //category
        //category videoList
        public int totalRecords;
        public int totalPnum;
        public List<ChildListBean> list;
    }


}
