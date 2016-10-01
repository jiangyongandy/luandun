package com.jy.xinlangweibo.ui.fragment.base;

import android.os.Bundle;

import com.jy.xinlangweibo.utils.ACache;

/**
 * Created by JIANG on 2016/9/1.
 */
public class BaseCacheFragment extends BaseFragment {

    protected ACache mCache;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(!NetworkUtils.isConnected(activity)) {
//            Logger.showLog("网络未连接得到缓存", "BaseFragment");
    //		得到缓存
            mCache = ACache.get(this.getActivity());
//        }
    }

}
