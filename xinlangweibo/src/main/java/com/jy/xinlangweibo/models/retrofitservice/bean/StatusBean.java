package com.jy.xinlangweibo.models.retrofitservice.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JIANG on 2016/10/1.
 */
public class StatusBean implements Serializable {

    /**
     * created_at : Thu Oct 06 03:12:01 +0800 2016
     * id : 4027417921649514
     * mid : 4027417921649514
     * idstr : 4027417921649514
     * text : #微博多封面图# 封面图可以上传9张图片啦！太棒了，美美哒图片终于可以一起展示啦，快来一起围观吧~ http://weibo.com/u/3201689587
     * textLength : 123
     * source_allowclick : 0
     * source_type : 1
     * source : <a href="http://app.weibo.com/t/feed/3cwKnY" rel="nofollow">微博客户端</a>
     * favorited : false
     * truncated : false
     * in_reply_to_status_id :
     * in_reply_to_user_id :
     * in_reply_to_screen_name :
     * pic_urls : [{"thumbnail_pic":"http://ww1.sinaimg.cn/thumbnail/4be1438djw1ez1qsc6i6uj20hs0hsmyb.jpg"},{"thumbnail_pic":"http://ww1.sinaimg.cn/thumbnail/bed5e7f3gw1f8hznmke62j20u00u0wln.jpg"}]
     * thumbnail_pic : http://ww1.sinaimg.cn/thumbnail/4be1438djw1ez1qsc6i6uj20hs0hsmyb.jpg
     * bmiddle_pic : http://ww1.sinaimg.cn/bmiddle/4be1438djw1ez1qsc6i6uj20hs0hsmyb.jpg
     * original_pic : http://ww1.sinaimg.cn/large/4be1438djw1ez1qsc6i6uj20hs0hsmyb.jpg
     * geo : null
     * annotations : [{"mapi_request":true}]
     * reposts_count : 0
     * comments_count : 0
     * attitudes_count : 0
     * isLongText : false
     * mlevel : 0
     * visible : {"type":0,"list_id":0}
     * biz_feature : 4294967300
     * page_type : 32
     * hasActionTypeCard : 0
     * darwin_tags : []
     * hot_weibo_tags : []
     * text_tag_tips : []
     * userType : 0
     * positive_recom_flag : 0
     * gif_ids :
     * is_show_bulletin : 2
     */
    public String created_at;
    public long id;
    public String mid;
    public String idstr;
    public String text;
    public int textLength;
    public int source_allowclick;
    public int source_type;
    public String source;
    public boolean favorited;
    public boolean truncated;
    public String in_reply_to_status_id;
    public String in_reply_to_user_id;
    public String in_reply_to_screen_name;
    public String thumbnail_pic;
    public String bmiddle_pic;
    public String original_pic;
    public UserBean user;
    public StatusBean retweeted_status;
    public Object geo;
    public int reposts_count;
    public int comments_count;
    public int attitudes_count;
    public boolean isLongText;
    public int mlevel;
    /**
     * type : 0
     * list_id : 0
     */

    public long biz_feature;
    public int page_type;
    public int hasActionTypeCard;
    public int userType;
    public int positive_recom_flag;
    public String gif_ids;
    public int is_show_bulletin;
    /**
     * thumbnail_pic : http://ww1.sinaimg.cn/thumbnail/4be1438djw1ez1qsc6i6uj20hs0hsmyb.jpg
     */

    public List<PicUrlsBean> pic_urls;
    public ArrayList<String> pic_urls2;
    /**
     * mapi_request : true
     */

    public List<AnnotationsBean> annotations;
    public List<?> darwin_tags;
    public List<?> hot_weibo_tags;
    public List<?> text_tag_tips;

    /**
     * 将解析后的图片URL集合  PicUrlsBean转换为字符串
     * @return
     */
    public ArrayList<String> getPic_urls() {
        if(pic_urls != null && pic_urls2 == null) {
            pic_urls2 = new ArrayList<>();
            for(PicUrlsBean pic:pic_urls)
                pic_urls2.add(pic.thumbnail_pic);
        }
        return pic_urls2;
    }

    public static class VisibleBean implements Serializable{
        public int type;
        public int list_id;
    }

    public static class PicUrlsBean implements Serializable{
        public String thumbnail_pic;
    }

    public static class AnnotationsBean implements Serializable{
        public boolean mapi_request;
    }
}
