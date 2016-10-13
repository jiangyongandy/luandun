package com.jy.xinlangweibo.models.bean;

import com.google.gson.Gson;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by JIANG on 2016/10/10.
 */
public class StatusBeanConverter implements PropertyConverter<Object, String> {
    @Override
    public StatusBean convertToEntityProperty(String databaseValue) {
        return new Gson().fromJson(databaseValue, StatusBean.class);
    }

    @Override
    public String convertToDatabaseValue(Object entityProperty) {
        return new Gson().toJson(entityProperty);
    }
}
