package com.jy.xinlangweibo.ui.adapter.videoinfosections;

import android.text.TextUtils;

import com.jy.xinlangweibo.models.net.BaseBean;
import com.jy.xinlangweibo.models.net.videoapi.videobean.VideoRes;
import com.jy.xinlangweibo.models.net.videoapi.videobean.VideoType;

import java.util.List;

/**
 * Created by JIANG on 2016/12/22.
 */

public class VideoInfoBean extends BaseBean {
        public VideoRes videoInfo;
        public List<VideoType> relationsList;
        public VideoRes comment;

        public String getVideoUrl() {
                if (!TextUtils.isEmpty(videoInfo.HDURL))
                        return videoInfo.HDURL;
                else if (!TextUtils.isEmpty(videoInfo.SDURL))
                        return videoInfo.SDURL;
                else if (!TextUtils.isEmpty(videoInfo.smoothURL))
                        return videoInfo.smoothURL;
                else
                        return "";
        }
}
