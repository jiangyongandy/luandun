package com.jiang.library.ui.adapter.recyleviewadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

/**
 * Created by JIANG on 2016/9/14.
 */
public interface IItemViewCreator<T extends Serializable> {

    View newContentView(LayoutInflater layoutInflater, ViewGroup parent, int viewType);

    /**
     * return a IITemView for bind data;
     * @param convertView
     * @param viewType
     * @return
     */
    IITemView<T> newItemView(View convertView, int viewType);
}
