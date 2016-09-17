package com.jiang.library.ui.adapter.recyleviewadapter;


import android.app.Activity;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.jiang.library.R;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by JIANG on 2016/9/14.
 */
public class BasicRecycleViewAdapter<T extends Serializable> extends Adapter {
    private IItemViewCreator<T> itemViewCreator;
    private ArrayList<T> datas;
    private IITemView<T> footerItemView;
    private AHeaderItemViewCreator<T> headerItemViewCreator;
    private int[][] headerItemTypes;
    private AdapterView.OnItemClickListener onItemClickListener;
    private AdapterView.OnItemLongClickListener onItemLongClickListener;
    View.OnClickListener innerOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            IITemView itemView = (IITemView) v.getTag(R.id.itemview);
            if (BasicRecycleViewAdapter.this.onItemClickListener != null && itemView != null) {
                BasicRecycleViewAdapter.this.onItemClickListener.onItemClick((AdapterView) null, itemView.getConvertView(), itemView.itemPosition(), BasicRecycleViewAdapter.this.getItemId(itemView.itemPosition()));
            }
        }
    };
    View.OnLongClickListener innerOnLongClickListener = new View.OnLongClickListener() {
        public boolean onLongClick(View v) {
            IITemView itemView = (IITemView) v.getTag(R.id.itemview);
            return BasicRecycleViewAdapter.this.onItemLongClickListener != null ? BasicRecycleViewAdapter.this.onItemLongClickListener.onItemLongClick((AdapterView) null, itemView.getConvertView(), itemView.itemPosition(), BasicRecycleViewAdapter.this.getItemId(itemView.itemPosition())) : false;
        }
    };

    public BasicRecycleViewAdapter(IItemViewCreator<T> itemViewCreator, ArrayList<T> datas) {
        if (datas == null) {
            datas = new ArrayList();
        }

        this.itemViewCreator = itemViewCreator;
        this.datas = datas;
    }

    public void addFooterView(IITemView<T> footerItemView) {
        if(footerItemView == null){
            this.footerItemView = null;
            return;
        }
        this.footerItemView = footerItemView;
        if (footerItemView.getConvertView().getLayoutParams() == null) {
            footerItemView.getConvertView().setLayoutParams(new LayoutParams(-1, -2));
        }

    }

    public void setHeaderItemViewCreator(AHeaderItemViewCreator<T> headerItemViewCreator) {
        this.headerItemViewCreator = headerItemViewCreator;
        this.headerItemTypes = headerItemViewCreator.createHeaders();
    }

    public int getItemViewType(int position) {
        if (this.footerItemView != null && position == this.getItemCount() - 1) {
            return 2000;
        } else if (this.headerItemViewCreator != null && position < this.headerItemTypes.length) {
            return this.headerItemTypes[position][1];
        } else {
            int headerCount = this.headerItemTypes != null ? this.headerItemTypes.length : 0;
            if (position >= headerCount) {
                int realPosition = position - headerCount;
                Serializable t = (Serializable) this.getDatas().get(realPosition);
                if (t instanceof ItemTypeData) {
                    return ((ItemTypeData) t).itemType();
                }
            }

            return 1000;
        }
    }

    private boolean isHeaderType(int viewType) {
        if (this.headerItemTypes != null) {
            int[][] arr$ = this.headerItemTypes;
            int len$ = arr$.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                int[] itemResAndType = arr$[i$];
                if (viewType == itemResAndType[1]) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isFooterType(int viewType) {
        return viewType == 2000;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView;
        IITemView itemView;
        if (this.isFooterType(viewType)) {
            itemView = this.footerItemView;
            convertView = itemView.getConvertView();
        } else if (this.isHeaderType(viewType)) {
            convertView = this.headerItemViewCreator.newContentView(((Activity) parent.getContext()).getLayoutInflater(), parent, viewType);
            itemView = this.headerItemViewCreator.newItemView(convertView, viewType);
            convertView.setTag(R.id.itemview, itemView);
        } else {
            convertView = this.itemViewCreator.newContentView(((Activity) parent.getContext()).getLayoutInflater(), parent, viewType);
            itemView = this.itemViewCreator.newItemView(convertView, viewType);
            convertView.setTag(R.id.itemview, itemView);
        }

        itemView.onBindView(convertView);
        if (!(itemView instanceof ARecycleViewItemView)) {
            throw new RuntimeException("RecycleView只支持ARecycleViewItemView，请重新配置");
        } else {
            return (ARecycleViewItemView) itemView;
        }
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        ARecycleViewItemView itemView = (ARecycleViewItemView) holder;
        int headerCount = this.headerItemTypes != null ? this.headerItemTypes.length : 0;
        if (position >= headerCount) {
            int realPosition = position - headerCount;
            itemView.reset(this.datas.size(), realPosition);
            if (realPosition < this.datas.size()) {
                itemView.onBindData(itemView.getConvertView(), (Serializable) this.datas.get(realPosition), realPosition);
            }else{
                itemView.onBindData(itemView.getConvertView(), null, realPosition);
            }
            if(position != getItemCount()-1) {
                if (this.onItemClickListener != null) {
                    itemView.getConvertView().setOnClickListener(this.innerOnClickListener);
                } else {
                    itemView.getConvertView().setOnClickListener((View.OnClickListener) null);
                }

                if (this.onItemLongClickListener != null) {
                    itemView.getConvertView().setOnLongClickListener(this.innerOnLongClickListener);
                } else {
                    itemView.getConvertView().setOnLongClickListener((View.OnLongClickListener) null);
                }
            }
        }

    }

    public int getItemCount() {
        int footerCount = this.footerItemView == null ? 0 : 1;
        int headerCount = this.headerItemTypes != null ? this.headerItemTypes.length : 0;
        return this.datas.size() + footerCount + headerCount;
    }

    public int getRealItemCount() {
        return this.datas.size();
    }

    /**
     * only return datas no contains header or footer
     * @param position
     * @return
     */
    public T getItem(int position) {
        return  datas.get(position);
    }

    public ArrayList<T> getDatas() {
        return this.datas;
    }

    public  void setDatas(ArrayList<T> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    /**
     * 删除指定索引数据条目
     *
     * @param position
     */
    public void removeItem(int position) {
        datas.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 移动数据条目的位置
     *
     * @param fromPosition
     * @param toPosition
     */
    public void moveItem(int fromPosition, int toPosition) {
        datas.add(toPosition, datas.remove(fromPosition));
        notifyItemMoved(fromPosition, toPosition);
    }

    public interface ItemTypeData {
        int itemType();
    }

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return this.onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AdapterView.OnItemLongClickListener getOnItemLongClickListener() {
        return this.onItemLongClickListener;
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
}

