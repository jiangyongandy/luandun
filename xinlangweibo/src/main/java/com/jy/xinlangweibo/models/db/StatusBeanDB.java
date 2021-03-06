package com.jy.xinlangweibo.models.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jy.xinlangweibo.dao.DaoMaster;
import com.jy.xinlangweibo.dao.DaoSession;
import com.jy.xinlangweibo.dao.StatusBeanDao;
import com.jy.xinlangweibo.models.net.sinaapi.sinabean.StatusBean;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by JIANG on 2016/10/11.
 */
public class StatusBeanDB {
    private final static String dbName = "test_db";
    public final static String userTimelineCacheType = "userTimeline";
    public final static String publicTimelineCacheType = "publicTimeline";
    private static StatusBeanDB mInstance;
    private LuanDuanDBHelper  openHelper;
    private Context context;

    public StatusBeanDB(Context context) {
        this.context = context;
        openHelper = new LuanDuanDBHelper(context, dbName, null);
    }

    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static StatusBeanDB getInstance(Context context) {
        if (mInstance == null) {
            synchronized (StatusBeanDB.class) {
                if (mInstance == null) {
                    mInstance = new StatusBeanDB(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 防止activity泄漏，静态引用持有activity
     */
    public static void destroy() {
        mInstance = null;
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new LuanDuanDBHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new LuanDuanDBHelper (context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

    /**
     * 插入一条记录
     *
     */
    public void insertStatusBean(StatusBean statusBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        StatusBeanDao statusBeanDao = daoSession.getStatusBeanDao();
//        StatusBeanDao statusBeanDao = daoSession.getStatusBeanDao();
        statusBeanDao.insert(statusBean);
/*//        插入一对一关系关联的记录
        statusBeanDao.insert(user.getStatus2());
//        循环插入一对多关系关联的记录
        for(StatusBean statusBean:user.getOders2()) {
            statusBeanDao.insert(statusBean);
        }*/
    }


    /**
     * 插入Ownerid字段为ownerid,CacheType等于cacheType的微博记录
     *
     */
    public void insertStatusBeanList(final List<StatusBean> statusBeans, long ownerId, String cacheType) {
        if (statusBeans == null || statusBeans.isEmpty()) {
            return;
        }
        for(StatusBean statusBean:statusBeans)
        {
            statusBean.cacheType = cacheType;
            statusBean.ownerId = ownerId;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        final StatusBeanDao statusBeanDao = daoSession.getStatusBeanDao();
        new Thread(new Runnable() {
            @Override
            public void run() {
                statusBeanDao.insertInTx(statusBeans);
                StatusBeanDB.destroy();
            }
        }).start();
    }

    /**
     * 删除Ownerid字段为ownerid,CacheType等于cacheType的微博记录
     *
     * @param ownerId 指定要删除ownerid等于ownerid的记录
     * @param cacheType 指定要删除cacheType等于cacheType的记录
     */
    public void deleteStatusBeanList(long ownerId,String cacheType) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        StatusBeanDao statusBeanDao = daoSession.getStatusBeanDao();
        DeleteQuery<StatusBean> statusBeanDeleteQuery = statusBeanDao.queryBuilder()
                .where(StatusBeanDao.Properties.OwnerId.eq(ownerId),
                        StatusBeanDao.Properties.CacheType.eq(cacheType))
                .buildDelete();
        statusBeanDeleteQuery.executeDeleteWithoutDetachingEntities();
    }

    /**
     * 删除一条记录
     *
     */
    public void deleteStatusBean(StatusBean statusBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        StatusBeanDao StatusBeanDao = daoSession.getStatusBeanDao();
        StatusBeanDao.delete(statusBean);
    }

    /**
     * 更新一条记录
     *
     */
    public void updateStatusBean(StatusBean statusBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        StatusBeanDao statusBeanDao = daoSession.getStatusBeanDao();
        statusBeanDao.update(statusBean);
    }

    /**
     * 查询用户列表
     */
    public List<StatusBean> queryStatusBeanList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        StatusBeanDao statusBeanDao = daoSession.getStatusBeanDao();
        QueryBuilder<StatusBean> qb = statusBeanDao.queryBuilder();
        List<StatusBean> list = qb.list();
        return list;
    }

    /**
     * 查询删Ownerid字段为ownerid,CacheType等于cacheType的微博记录
     * 当查询的记录为空时该方法也不会返回空指针，所以使用此方法应该以size的大小来判断是否有记录
     */
    public List<StatusBean> queryStatusBeanList(long ownerId,String cacheType) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        StatusBeanDao statusBeanDao = daoSession.getStatusBeanDao();
        QueryBuilder<StatusBean> qb = statusBeanDao.queryBuilder();
        List<StatusBean> list = qb.where(StatusBeanDao.Properties.OwnerId.eq(ownerId),
                                        StatusBeanDao.Properties.CacheType.eq(cacheType))
                                        .list();
        return list;
    }

    public Callable<List<StatusBean>> queryFuzzyStatusBeanList(final long ownerId, final String like) {

        return new Callable<List<StatusBean>>() {
            @Override
            public List<StatusBean> call() {
                // select * from users where _id is userId
                DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
                DaoSession daoSession = daoMaster.newSession();
                StatusBeanDao statusBeanDao = daoSession.getStatusBeanDao();
                QueryBuilder<StatusBean> qb = statusBeanDao.queryBuilder();
                List<StatusBean> list = qb.where(StatusBeanDao.Properties.OwnerId.eq(ownerId),
                        StatusBeanDao.Properties.Text.like("%"+like+"%")
                ).list();
                System.out.println("queryFuzzyStatusBeanList-----"+list);
                return list;
            }
        };
    }

    public void queryFuzzyStatusBeanList2(final long ownerId, final String like, Observer<List<String>> observer) {
        makeObservable(queryFuzzyStatusBeanList(ownerId,like))
                .map(new Func1<List<StatusBean>, List<String>>() {
                    @Override
                    public List<String> call(List<StatusBean> statusBeens) {
                        ArrayList<String> strings = new ArrayList<String>();
                        for(StatusBean statusBean:statusBeens) {
                            strings.add(statusBean.text);
                        }
                        return strings;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
                 // note: do not use Schedulers.io()
    }

    private static <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(
                new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        try {
                            subscriber.onNext(func.call());
                        } catch(Exception ex) {
                            Log.e("StatusBeanDB", "Error reading from the database", ex);
                        }
                    }
                });
    }

}
