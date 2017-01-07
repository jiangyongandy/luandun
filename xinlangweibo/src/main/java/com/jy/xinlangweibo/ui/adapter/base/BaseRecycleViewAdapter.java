package com.jy.xinlangweibo.ui.adapter.base;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JIANG on 2017/1/7.
 */

public abstract class BaseRecycleViewAdapter<T extends Serializable> extends RecyclerView.Adapter {

    private List<T> list;
    protected Activity activity;

    public BaseRecycleViewAdapter(List<T> list) {
        this.list = list;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        activity = (Activity) parent.getContext();
        View itemView = activity.getLayoutInflater().inflate(createView(), parent, false);
        return createViewHolder(itemView);
    }


    protected T getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected abstract int createView();

    protected abstract RecyclerView.ViewHolder createViewHolder(View itemView);
}
