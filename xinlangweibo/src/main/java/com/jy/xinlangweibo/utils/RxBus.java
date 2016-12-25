package com.jy.xinlangweibo.utils;


import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by hcc on 16/8/14 17:51
 * 100332338@qq.com
 * RxBus
 * �¼�����
 */
public class RxBus
{

    private static volatile RxBus mInstance;

    private final Subject<Object,Object> bus;


    private RxBus()
    {

        bus = new SerializedSubject(PublishSubject.create());
    }

    /**
     * 单例模式
     *
     * @return
     */
    public static RxBus getInstance()
    {

        RxBus rxBus = mInstance;
        if (mInstance == null)
        {
            synchronized (RxBus.class)
            {
                rxBus = mInstance;
                if (mInstance == null)
                {
                    rxBus = new RxBus();
                    mInstance = rxBus;
                }
            }
        }

        return rxBus;
    }


    /**
     * 发送事件
     *
     * @param object
     */
    public void post(Object object)
    {

        bus.onNext(object);
    }

    /**
     * 接受事件
     *
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObserverable(Class<T> eventType)
    {

        return bus.ofType(eventType);
    }
}
