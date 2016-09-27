package com.jy.xinlangweibo.bean;

import java.io.Serializable;

/**
 * Created by JIANG on 2016/9/21.
 */
public class PublishBean implements Serializable {

    public enum PublishStatus {
        // 新建
        create,
        // 发布失败
        faild,
        // 草稿
        draft,
        // 正在发布
        sending,
        // 等待发布
        waiting
    }

    private PublishStatus status;

    String[] pics;
    private String textContent;


    public PublishStatus getStatus() {
        return status;
    }

    public void setStatus(PublishStatus status) {
        this.status = status;
    }

    public String[] getPics() {
        return pics;
    }

    public void setPics(String[] pics) {
        this.pics = pics;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getTextContent() {
        return textContent;
    }
}
