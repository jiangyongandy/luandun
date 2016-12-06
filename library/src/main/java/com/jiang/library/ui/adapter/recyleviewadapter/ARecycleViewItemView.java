package com.jiang.library.ui.adapter.recyleviewadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.Serializable;

import butterknife.ButterKnife;

/**
 * Created by JIANG on 2016/9/14.
 */
public  class ARecycleViewItemView<T extends Serializable> extends RecyclerView.ViewHolder implements IITemView<T> {
    private int size;
    private int position;
    private View convertView;
    protected Context context;

    public ARecycleViewItemView(Context context, View itemView) {
        super(itemView);
        this.context = context;
        this.convertView = itemView;
    }

    public ARecycleViewItemView(View itemView) {
        super(itemView);
        this.convertView = itemView;
    }

    public void onBindView(View convertView) {
//        InjectUtility.initInjectedView(this.getContext(), this, convertView);
        ButterKnife.bind(this,convertView);
    }

    @Override
    public void onBindData(View convertView, T model, int position) {

    }

    public void reset(int size, int position) {
        this.size = size;
        this.position = position;
    }

    public int itemSize() {
        return this.size;
    }

    public int itemPosition() {
        return this.position;
    }

    public View getConvertView() {
        return this.convertView;
    }

    public Context getContext() {
        return this.context;
    }
}
