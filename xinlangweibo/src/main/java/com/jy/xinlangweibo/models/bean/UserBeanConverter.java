package com.jy.xinlangweibo.models.bean;

import com.google.gson.Gson;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by JIANG on 2016/10/11.
 */
public class UserBeanConverter implements PropertyConverter<UserBean, String> {
    @Override
    public UserBean convertToEntityProperty(String databaseValue) {
        return new Gson().fromJson(databaseValue, UserBean.class);
    }

    @Override
    public String convertToDatabaseValue(UserBean entityProperty) {
        return new Gson().toJson(entityProperty);
    }
}
