package com.jy.xinlangweibo.models.net.sinaapi.sinabean;

import java.io.Serializable;

/**
 * Created by JIANG on 2016/11/12.
 */

public class CommentBean implements Serializable{
    /** 评论创建时间 */
    public String created_at;
    /** 评论的 ID */
    public String id;
    /** 评论的内容 */
    public String text;
    /** 评论的来源 */
    public String source;
    /** 评论作者的用户信息字段 */
    public UserBean user;
    /** 评论的 MID */
    public String mid;
    /** 字符串型的评论 ID */
    public String idstr;
    /** 评论的微博信息字段 */
    public StatusBean status;
    /** 评论来源评论，当本评论属于对另一评论的回复时返回此字段 */
    public CommentBean reply_comment;
}
