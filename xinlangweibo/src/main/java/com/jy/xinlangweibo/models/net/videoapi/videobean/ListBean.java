package com.jy.xinlangweibo.models.net.videoapi.videobean;

import com.jy.xinlangweibo.models.net.BaseBean;

import java.util.List;

/**
 * Created by JIANG on 2016/12/5.
 */

public class ListBean extends BaseBean{
    public String showStyle;
    public String loadType;
    public String changeOpenFlag;
    public int line;
    public String showType;
    public String moreURL;
    public String title;
    public String bigPicShowFlag;
    public List<ChildListBean> childList;
}
