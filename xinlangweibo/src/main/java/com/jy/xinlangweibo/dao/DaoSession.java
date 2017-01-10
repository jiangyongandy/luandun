package com.jy.xinlangweibo.dao;

import com.jy.xinlangweibo.models.net.sinaapi.sinabean.StatusBean;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig statusBeanDaoConfig;

    private final StatusBeanDao statusBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        statusBeanDaoConfig = daoConfigMap.get(StatusBeanDao.class).clone();
        statusBeanDaoConfig.initIdentityScope(type);

        statusBeanDao = new StatusBeanDao(statusBeanDaoConfig, this);

        registerDao(StatusBean.class, statusBeanDao);
    }
    
    public void clear() {
        statusBeanDaoConfig.getIdentityScope().clear();
    }

    public StatusBeanDao getStatusBeanDao() {
        return statusBeanDao;
    }

}
