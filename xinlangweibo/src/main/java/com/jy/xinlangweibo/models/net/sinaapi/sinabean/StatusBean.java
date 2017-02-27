package com.jy.xinlangweibo.models.net.sinaapi.sinabean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JIANG on 2016/10/1.
 */

//这里为数据库中的字段，不要轻易修改，如果要修改，考虑数据库升级策略
@Entity
public class StatusBean implements Serializable {
//    额外在数据库中增加的字段，不能与接口中的字段重名
//    表示该条缓存记录所属，用用户的uid作为实际值
    public long ownerId;
//    表示缓存类型 userTimeline/publicTimeLine
    public String cacheType;
//    本地数据库自增主键
//    @Id(autoincrement = true)
//    public long localId;


//    接口字段
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
    @Convert(converter = UserBeanConverter.class, columnType = String.class)
    public UserBean user;
    @Convert(converter = StatusBeanConverter.class, columnType = String.class)
    public StatusBean retweeted_status;
    @Convert(converter = GeoBeanConverter.class, columnType = String.class)
    public GeoBean geo;
    public int reposts_count;
    public int comments_count;
    public int attitudes_count;
    public boolean isLongText;
    public int mlevel;
    public long biz_feature;
    public int page_type;
    public int hasActionTypeCard;
    public int userType;
    public int positive_recom_flag;
    public String gif_ids;
    public int is_show_bulletin;
    @Convert(converter = PicUrlsBeanConverter.class, columnType = String.class)
    public List<PicUrlsBean> pic_urls;
    @Transient
    public ArrayList<String> pic_urls2;
    @Generated(hash = 400915282)
    public StatusBean(long ownerId, String cacheType, String created_at, long id,
            String mid, String idstr, String text, int textLength,
            int source_allowclick, int source_type, String source,
            boolean favorited, boolean truncated, String in_reply_to_status_id,
            String in_reply_to_user_id, String in_reply_to_screen_name,
            String thumbnail_pic, String bmiddle_pic, String original_pic,
            UserBean user, StatusBean retweeted_status, GeoBean geo,
            int reposts_count, int comments_count, int attitudes_count,
            boolean isLongText, int mlevel, long biz_feature, int page_type,
            int hasActionTypeCard, int userType, int positive_recom_flag,
            String gif_ids, int is_show_bulletin, List<PicUrlsBean> pic_urls) {
        this.ownerId = ownerId;
        this.cacheType = cacheType;
        this.created_at = created_at;
        this.id = id;
        this.mid = mid;
        this.idstr = idstr;
        this.text = text;
        this.textLength = textLength;
        this.source_allowclick = source_allowclick;
        this.source_type = source_type;
        this.source = source;
        this.favorited = favorited;
        this.truncated = truncated;
        this.in_reply_to_status_id = in_reply_to_status_id;
        this.in_reply_to_user_id = in_reply_to_user_id;
        this.in_reply_to_screen_name = in_reply_to_screen_name;
        this.thumbnail_pic = thumbnail_pic;
        this.bmiddle_pic = bmiddle_pic;
        this.original_pic = original_pic;
        this.user = user;
        this.retweeted_status = retweeted_status;
        this.geo = geo;
        this.reposts_count = reposts_count;
        this.comments_count = comments_count;
        this.attitudes_count = attitudes_count;
        this.isLongText = isLongText;
        this.mlevel = mlevel;
        this.biz_feature = biz_feature;
        this.page_type = page_type;
        this.hasActionTypeCard = hasActionTypeCard;
        this.userType = userType;
        this.positive_recom_flag = positive_recom_flag;
        this.gif_ids = gif_ids;
        this.is_show_bulletin = is_show_bulletin;
        this.pic_urls = pic_urls;
    }

    @Generated(hash = 1646314665)
    public StatusBean() {
    }
    /**
     * mapi_request : true
     */
    /**
     * 将解析后的图片URL集合  PicUrlsBean转换为字符串
     * @return
     */
    public ArrayList<String> getPic_urls2() {
        if(pic_urls != null && pic_urls2 == null) {
            pic_urls2 = new ArrayList<String>();
            for(PicUrlsBean pic:pic_urls)
                pic_urls2.add(pic.thumbnail_pic);
        }
        return pic_urls2;
    }

    public static List<PicUrlsBean> arrayStatusBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<PicUrlsBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public List<PicUrlsBean> getPic_urls() {
        return this.pic_urls;
    }

    public void setPic_urls(List<PicUrlsBean> pic_urls) {
        this.pic_urls = pic_urls;
    }

    public int getIs_show_bulletin() {
        return this.is_show_bulletin;
    }

    public void setIs_show_bulletin(int is_show_bulletin) {
        this.is_show_bulletin = is_show_bulletin;
    }

    public String getGif_ids() {
        return this.gif_ids;
    }

    public void setGif_ids(String gif_ids) {
        this.gif_ids = gif_ids;
    }

    public int getPositive_recom_flag() {
        return this.positive_recom_flag;
    }

    public void setPositive_recom_flag(int positive_recom_flag) {
        this.positive_recom_flag = positive_recom_flag;
    }

    public int getUserType() {
        return this.userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getHasActionTypeCard() {
        return this.hasActionTypeCard;
    }

    public void setHasActionTypeCard(int hasActionTypeCard) {
        this.hasActionTypeCard = hasActionTypeCard;
    }

    public int getPage_type() {
        return this.page_type;
    }

    public void setPage_type(int page_type) {
        this.page_type = page_type;
    }

    public long getBiz_feature() {
        return this.biz_feature;
    }

    public void setBiz_feature(long biz_feature) {
        this.biz_feature = biz_feature;
    }

    public int getMlevel() {
        return this.mlevel;
    }

    public void setMlevel(int mlevel) {
        this.mlevel = mlevel;
    }

    public boolean getIsLongText() {
        return this.isLongText;
    }

    public void setIsLongText(boolean isLongText) {
        this.isLongText = isLongText;
    }

    public int getAttitudes_count() {
        return this.attitudes_count;
    }

    public void setAttitudes_count(int attitudes_count) {
        this.attitudes_count = attitudes_count;
    }

    public int getComments_count() {
        return this.comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getReposts_count() {
        return this.reposts_count;
    }

    public void setReposts_count(int reposts_count) {
        this.reposts_count = reposts_count;
    }

    public GeoBean getGeo() {
        return this.geo;
    }

    public void setGeo(GeoBean geo) {
        this.geo = geo;
    }

    public StatusBean getRetweeted_status() {
        return this.retweeted_status;
    }

    public void setRetweeted_status(StatusBean retweeted_status) {
        this.retweeted_status = retweeted_status;
    }

    public UserBean getUser() {
        return this.user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getOriginal_pic() {
        return this.original_pic;
    }

    public void setOriginal_pic(String original_pic) {
        this.original_pic = original_pic;
    }

    public String getBmiddle_pic() {
        return this.bmiddle_pic;
    }

    public void setBmiddle_pic(String bmiddle_pic) {
        this.bmiddle_pic = bmiddle_pic;
    }

    public String getThumbnail_pic() {
        return this.thumbnail_pic;
    }

    public void setThumbnail_pic(String thumbnail_pic) {
        this.thumbnail_pic = thumbnail_pic;
    }

    public String getIn_reply_to_screen_name() {
        return this.in_reply_to_screen_name;
    }

    public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
        this.in_reply_to_screen_name = in_reply_to_screen_name;
    }

    public String getIn_reply_to_user_id() {
        return this.in_reply_to_user_id;
    }

    public void setIn_reply_to_user_id(String in_reply_to_user_id) {
        this.in_reply_to_user_id = in_reply_to_user_id;
    }

    public String getIn_reply_to_status_id() {
        return this.in_reply_to_status_id;
    }

    public void setIn_reply_to_status_id(String in_reply_to_status_id) {
        this.in_reply_to_status_id = in_reply_to_status_id;
    }

    public boolean getTruncated() {
        return this.truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public boolean getFavorited() {
        return this.favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getSource_type() {
        return this.source_type;
    }

    public void setSource_type(int source_type) {
        this.source_type = source_type;
    }

    public int getSource_allowclick() {
        return this.source_allowclick;
    }

    public void setSource_allowclick(int source_allowclick) {
        this.source_allowclick = source_allowclick;
    }

    public int getTextLength() {
        return this.textLength;
    }

    public void setTextLength(int textLength) {
        this.textLength = textLength;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIdstr() {
        return this.idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public String getMid() {
        return this.mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public long getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public String getCacheType() {
        return this.cacheType;
    }

    public void setCacheType(String cacheType) {
        this.cacheType = cacheType;
    }

    @Override
    public String toString() {
        return "StatusBean{" +
                "ownerId=" + ownerId +
                ", cacheType='" + cacheType + '\'' +
                ", created_at='" + created_at + '\'' +
                ", id=" + id +
                ", mid='" + mid + '\'' +
                ", idstr='" + idstr + '\'' +
                ", text='" + text + '\'' +
                ", textLength=" + textLength +
                ", source_allowclick=" + source_allowclick +
                ", source_type=" + source_type +
                ", source='" + source + '\'' +
                ", favorited=" + favorited +
                ", truncated=" + truncated +
                ", in_reply_to_status_id='" + in_reply_to_status_id + '\'' +
                ", in_reply_to_user_id='" + in_reply_to_user_id + '\'' +
                ", in_reply_to_screen_name='" + in_reply_to_screen_name + '\'' +
                ", thumbnail_pic='" + thumbnail_pic + '\'' +
                ", bmiddle_pic='" + bmiddle_pic + '\'' +
                ", original_pic='" + original_pic + '\'' +
                ", user=" + user +
                ", retweeted_status=" + retweeted_status +
                ", geo=" + geo +
                ", reposts_count=" + reposts_count +
                ", comments_count=" + comments_count +
                ", attitudes_count=" + attitudes_count +
                ", isLongText=" + isLongText +
                ", mlevel=" + mlevel +
                ", biz_feature=" + biz_feature +
                ", page_type=" + page_type +
                ", hasActionTypeCard=" + hasActionTypeCard +
                ", userType=" + userType +
                ", positive_recom_flag=" + positive_recom_flag +
                ", gif_ids='" + gif_ids + '\'' +
                ", is_show_bulletin=" + is_show_bulletin +
                ", pic_urls=" + pic_urls +
                ", pic_urls2=" + pic_urls2 +
                '}';
    }

}
