package com.jy.xinlangweibo.models.bean;

import com.google.gson.Gson;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

/**
 * Created by JIANG on 2016/10/11.
 */
public class PicUrlsBeanConverter implements PropertyConverter<List<PicUrlsBean>, String> {
    @Override
    public List<PicUrlsBean> convertToEntityProperty(String databaseValue) {
        return StatusBean.arrayStatusBeanFromData(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(List<PicUrlsBean> entityProperty) {
        return new Gson().toJson(entityProperty);
    }
}
