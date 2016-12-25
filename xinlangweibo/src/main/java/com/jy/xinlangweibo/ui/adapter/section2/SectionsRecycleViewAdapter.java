package com.jy.xinlangweibo.ui.adapter.section2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jy.xinlangweibo.ui.adapter.section.Section;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JIANG on 2016/12/21.
 */

public class SectionsRecycleViewAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_QTY = 1;
    private List<Section> sections;
    private List<Integer> sectionViewTypeNumbers;
    private int viewTypeCount;

    public SectionsRecycleViewAdapter() {
        sections = new ArrayList<>();
        sectionViewTypeNumbers = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionViewTypeNumbers.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Section section = sections.get(viewType);
        View view = LayoutInflater.from(parent.getContext()).inflate(section.getItemResourceId(), parent, false);
        // get the item viewholder from the section
        return sections.get(viewType).getItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void addSection(Section section)
    {

        this.sections.add(section);
        this.sectionViewTypeNumbers.add(viewTypeCount);
        viewTypeCount += VIEW_TYPE_QTY;
    }
}
