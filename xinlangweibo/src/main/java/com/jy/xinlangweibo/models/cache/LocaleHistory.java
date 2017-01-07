package com.jy.xinlangweibo.models.cache;

import com.jy.xinlangweibo.BaseApplication;
import com.jy.xinlangweibo.models.net.videoapi.videobean.ChildListBean;
import com.jy.xinlangweibo.utils.ACache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JIANG on 2017/1/6.
 */

public class LocaleHistory {

    private static LocaleHistory instance;

    private static final String VIDEO_SEARCH_HISTORY = "videoSearchHistory";

    private static final String VIDEO_PLAY_HISTORY = "videoPlayHistory";

    private ACache mCache;

    public static LocaleHistory getInstance() {
        if(instance == null)
            instance = new LocaleHistory();
        return instance;
    }
//todo IO操作应该新开线程
    private LocaleHistory() {
        mCache = ACache.get(BaseApplication.getInstance());
    }

    public void updateVideoSearchHistory(List searchHistoryStrings) {
        mCache.put(VIDEO_SEARCH_HISTORY, (ArrayList) searchHistoryStrings, 24 * 60 * 60);
    }

    public List<String> getVideoSearchHistory() {
        return (List<String>) mCache.getAsObject(VIDEO_SEARCH_HISTORY);
    }

    public boolean clearVideoSearchHistory() {
        return mCache.remove(VIDEO_SEARCH_HISTORY);
    }

    public void addVideoPlayHistory(ChildListBean bean) {
        List<ChildListBean> playHistory = getVideoPlayHistory();
        if(playHistory == null)
            playHistory = new ArrayList<>();
        if( playHistory.size() > 0 && playHistory.get(0).title.equals(bean.title) ) {
            return;
        }
        if(playHistory.size() == 5)
            playHistory.remove(4);
        playHistory.add(0,bean);
        mCache.put(VIDEO_PLAY_HISTORY, (ArrayList) playHistory, 24 * 60 * 60);
    }

    public List<ChildListBean> getVideoPlayHistory() {
        return (List<ChildListBean>) mCache.getAsObject(VIDEO_PLAY_HISTORY);
    }

    public boolean clearVideoPlayHistory() {
        return mCache.remove(VIDEO_PLAY_HISTORY);
    }

}
