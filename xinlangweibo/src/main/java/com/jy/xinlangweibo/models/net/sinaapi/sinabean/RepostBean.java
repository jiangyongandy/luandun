package com.jy.xinlangweibo.models.net.sinaapi.sinabean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JIANG on 2016/11/13.
 */

public class RepostBean  implements Serializable{

    public String created_at;
    public long id;
    public String mid;
    public String idstr;
    public String text;
    public int source_allowclick;
    public int source_type;
    public String source;
    public boolean favorited;
    public boolean truncated;
    public String in_reply_to_status_id;
    public String in_reply_to_user_id;
    public String in_reply_to_screen_name;
    public Object geo;
    public UserBean user;
    public StatusBean retweeted_status;
    public int reposts_count;
    public int comments_count;
    public int attitudes_count;
    public boolean isLongText;
    public int mlevel;
    public int biz_feature;
    public int hasActionTypeCard;
    public int userType;
    public int positive_recom_flag;
    public String gif_ids;
    public int is_show_bulletin;
    public List<?> pic_urls;
    public List<?> darwin_tags;
    public List<?> hot_weibo_tags;
    public List<?> text_tag_tips;

    @Override
    public String toString() {
        return "RepostBean{" +
                "created_at='" + created_at + '\'' +
                ", id=" + id +
                ", mid='" + mid + '\'' +
                ", idstr='" + idstr + '\'' +
                ", text='" + text + '\'' +
                ", source_allowclick=" + source_allowclick +
                ", source_type=" + source_type +
                ", source='" + source + '\'' +
                ", favorited=" + favorited +
                ", truncated=" + truncated +
                ", in_reply_to_status_id='" + in_reply_to_status_id + '\'' +
                ", in_reply_to_user_id='" + in_reply_to_user_id + '\'' +
                ", in_reply_to_screen_name='" + in_reply_to_screen_name + '\'' +
                ", geo=" + geo +
                ", user=" + user +
                ", retweeted_status=" + retweeted_status +
                ", reposts_count=" + reposts_count +
                ", comments_count=" + comments_count +
                ", attitudes_count=" + attitudes_count +
                ", isLongText=" + isLongText +
                ", mlevel=" + mlevel +
                ", biz_feature=" + biz_feature +
                ", hasActionTypeCard=" + hasActionTypeCard +
                ", userType=" + userType +
                ", positive_recom_flag=" + positive_recom_flag +
                ", gif_ids='" + gif_ids + '\'' +
                ", is_show_bulletin=" + is_show_bulletin +
                ", pic_urls=" + pic_urls +
                ", darwin_tags=" + darwin_tags +
                ", hot_weibo_tags=" + hot_weibo_tags +
                ", text_tag_tips=" + text_tag_tips +
                '}';
    }
}
