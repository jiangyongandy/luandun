package com.jiang.library.ui;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import java.io.Serializable;

/**
 * Created by JIANG on 2016/10/4.
 */
public abstract class BaseViewHolder<T extends Serializable> implements IAView<T> {
    protected Context context;
    private View convertView;
    protected int layoutRes ;

    public BaseViewHolder(Context context) {
        this.context = context;
        this.convertView = ((Activity)context).getLayoutInflater().inflate(getLayout(),null,false);
        onBindView(convertView);
    }

    public BaseViewHolder(Context context,View convertView) {
        this.context = context;
        this.convertView = convertView;
        onBindView(convertView);
    }

    @Override
    public abstract void onBindView(View convertView);

    @Override
    public abstract void bindData(T model);


    @Override
    public View getConvertView() {
        return convertView;
    }
}
