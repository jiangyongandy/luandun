package com.jy.xinlangweibo.ui.adapter.base;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JIANG on 2017/1/7.
 */

public abstract class BaseRecycleViewAdapter<T extends Serializable> extends RecyclerView.Adapter {

    private List<T> list;
    protected Activity activity;
    private AdapterView.OnItemClickListener onItemClickListener;
    View.OnClickListener innerOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            RecyclerView.ViewHolder itemView = (RecyclerView.ViewHolder) v.getTag();
            if (BaseRecycleViewAdapter.this.onItemClickListener != null && itemView != null) {
                int position = ((RecyclerView) itemView.itemView.getParent()).getChildAdapterPosition(itemView.itemView);
                BaseRecycleViewAdapter.this.onItemClickListener.onItemClick((AdapterView) null, itemView.itemView, position , BaseRecycleViewAdapter.this.getItemId(position));
            }else if(BaseRecycleViewAdapter.this.onItemClickListener != null && itemView == null) {

            }
        }
    };

    public BaseRecycleViewAdapter(List<T> list) {
        this.list = list;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        activity = (Activity) parent.getContext();
        View itemView = activity.getLayoutInflater().inflate(createView(), parent, false);
        RecyclerView.ViewHolder viewHolder = createViewHolder(itemView);
        itemView.setTag(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(this.innerOnClickListener);
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

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
