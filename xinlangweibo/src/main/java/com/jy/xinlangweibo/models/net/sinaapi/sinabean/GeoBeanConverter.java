package com.jy.xinlangweibo.models.net.sinaapi.sinabean;

import com.google.gson.Gson;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by JIANG on 2016/10/11.
 */
public class GeoBeanConverter implements PropertyConverter<GeoBean, String> {
    @Override
    public GeoBean convertToEntityProperty(String databaseValue) {
        return new Gson().fromJson(databaseValue, GeoBean.class);
    }

    @Override
    public String convertToDatabaseValue(GeoBean entityProperty) {
        return new Gson().toJson(entityProperty);
    }
}
