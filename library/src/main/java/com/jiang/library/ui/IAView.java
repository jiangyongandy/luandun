package com.jiang.library.ui;

import android.view.View;

import java.io.Serializable;

/**
 * Created by JIANG on 2016/10/4.
 */
public interface IAView<T extends Serializable> {
    int getLayout();

    void onBindView(View convertView);

    void bindData(T model);

    View getConvertView();
}
