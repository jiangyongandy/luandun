package com.jy.xinlangweibo.models.retrofitservice.bean;

/**
 * Created by JIANG on 2016/9/24.
 */
public class UploadPicResultBean {
    private static final long serialVersionUID = -5742423620822198794L;

    private String pic_id;

    private String thumbnail_pic;

    public String getPic_id() {
        return pic_id;
    }

    public void setPic_id(String pic_id) {
        this.pic_id = pic_id;
    }

    public String getThumbnail_pic() {
        return thumbnail_pic;
    }

    public void setThumbnail_pic(String thumbnail_pic) {
        this.thumbnail_pic = thumbnail_pic;
    }
}
