package com.jiang.library.ui.adapter.recyleviewadapter;

import android.view.View;

import java.io.Serializable;

/**
 * Created by JIANG on 2016/9/14.
 */
public interface IITemView<T extends Serializable> {
    void onBindView(View convertView);

    void onBindData(View convertView, T model, int position);

    int itemPosition();

    void reset(int size, int position);

    int itemSize();

    View getConvertView();
}
