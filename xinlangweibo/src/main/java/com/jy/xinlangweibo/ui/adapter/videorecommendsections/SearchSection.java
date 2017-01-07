package com.jy.xinlangweibo.ui.adapter.videorecommendsections;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.ui.adapter.section.SectionedRecyclerViewAdapter;
import com.jy.xinlangweibo.ui.adapter.section.StatelessSection;
import com.jy.xinlangweibo.ui.fragment.dialog.SearchDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JIANG on 2016/12/26.
 */

public class SearchSection extends StatelessSection {

    private Context mContext;

    public SearchSection(Context context) {
        super(R.layout.layout_custom_searchview, R.layout.layout_home_recommend_empty);
        this.mContext = context;
    }

    @Override
    public int getContentItemsTotal() {
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new SectionedRecyclerViewAdapter.EmptyViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        SearchViewHolder viewHolder = (SearchViewHolder) holder;

        viewHolder.tvSearchHint.setText("搜索视频");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchDialogFragment.getSearchDialogFragment().show(((AppCompatActivity) mContext).getSupportFragmentManager(), "SearchDialogFragment");
            }
        });
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder {
//        R.layout.layout_custom_searchview
        @BindView(R.id.tv_search_hint)
        TextView tvSearchHint;


        SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
