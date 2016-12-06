package com.jy.xinlangweibo.models.net.sinaapi.sinabean;

import java.util.List;

/**
 * Created by JIANG on 2016/11/12.
 */

public class CommentListBean {
    public boolean hasvisible;
    public int previous_cursor;
    public int total_number;
    public List<CommentBean > comments;

    @Override
    public String toString() {
        return "CommentListBean{" +
                "hasvisible=" + hasvisible +
                ", previous_cursor=" + previous_cursor +
                ", total_number=" + total_number +
                ", comments=" + comments +
                '}';
    }
}
