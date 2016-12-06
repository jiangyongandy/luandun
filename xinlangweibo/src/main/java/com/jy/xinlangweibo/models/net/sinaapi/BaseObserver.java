package com.jy.xinlangweibo.models.net.sinaapi;


import com.jy.xinlangweibo.utils.Logger;

import rx.Observer;

/**
 * Created by JIANG on 2016/10/5.
 */
public class BaseObserver<T> implements Observer<T> {
    @Override
    public void onCompleted() {
        Logger.showLog("事件序列处理完成==============","onCompleted:"+this);
    }

    @Override
    public void onError(Throwable e) {
        Logger.showLog("错误==============="+e.getMessage(),"onError:"+this);
    }

    @Override
    public void onNext(T models) {
        Logger.showLog("接受到事件序列================"+models,"onNext:"+this);
    }
}
