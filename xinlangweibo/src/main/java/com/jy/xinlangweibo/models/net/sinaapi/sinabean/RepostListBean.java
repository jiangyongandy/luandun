package com.jy.xinlangweibo.models.net.sinaapi.sinabean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JIANG on 2016/11/13.
 */

public class RepostListBean implements Serializable {


    public boolean hasvisible;
    public long previous_cursor;
    public long next_cursor;
    public int total_number;
    public int interval;
    public List<RepostBean> reposts;

    @Override
    public String toString() {
        return "RepostListBean{" +
                "hasvisible=" + hasvisible +
                ", previous_cursor=" + previous_cursor +
                ", next_cursor=" + next_cursor +
                ", total_number=" + total_number +
                ", interval=" + interval +
                ", reposts=" + reposts +
                '}';
    }
}
