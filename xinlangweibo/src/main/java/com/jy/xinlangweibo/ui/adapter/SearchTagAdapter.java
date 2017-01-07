package com.jy.xinlangweibo.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jy.xinlangweibo.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;

/**
 * Created by JIANG on 2016/12/27.
 */

public class SearchTagAdapter extends TagAdapter<String> {
    public SearchTagAdapter(List datas) {
        super(datas);
    }

    @Override
    public View getView(FlowLayout parent, int position, String keyWord) {
        TextView mTags = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tag, parent, false);
        mTags.setText(keyWord);
        return mTags;
    }
}
