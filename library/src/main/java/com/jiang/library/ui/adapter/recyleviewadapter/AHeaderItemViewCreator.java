package com.jiang.library.ui.adapter.recyleviewadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

/**
 * Created by JIANG on 2016/9/14.
 */
public abstract class AHeaderItemViewCreator<T extends Serializable> implements IItemViewCreator<T> {
    public AHeaderItemViewCreator() {
    }

    public View newContentView(LayoutInflater inflater, ViewGroup parent, int viewType) {
        int[][] arr = this.createHeaders();
        int len = arr.length;

        for(int i = 0; i < len; ++i) {
            int[] headerLayoutRes = arr[i];
            if(viewType == headerLayoutRes[1]) {
                return inflater.inflate(headerLayoutRes[0], parent, false);
            }
        }

        return null;
    }

    public abstract int[][] createHeaders();
}
