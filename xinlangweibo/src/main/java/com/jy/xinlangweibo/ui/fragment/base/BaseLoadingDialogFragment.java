package com.jy.xinlangweibo.ui.fragment.base;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jy.xinlangweibo.R;

/**
 * Created by JIANG on 2016/12/26.
 */
//todo 未完成
public abstract class BaseLoadingDialogFragment extends DialogFragment {

    private View loadingView;
    private ViewGroup parentView;
    private int viewIndex;
    private ViewGroup.LayoutParams params;
    private View currentView;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.loadingView = getLoadingTargetView();
    }

    protected abstract void loadData();

    protected abstract void updateUi() ;

    protected abstract View getLoadingTargetView();

    protected void showLoading() {
        String msg = "";
        View layout = LayoutInflater.from(loadingView.getContext()).inflate(R.layout.loading, null);
        if (!msg.isEmpty()) {
            TextView textView = (TextView) layout.findViewById(R.id.loading_msg);
            textView.setText(msg);
        }
        showLayout(layout);
    }

    protected void showErrorMessage() {
        String msg = "";
        View layout = LayoutInflater.from(loadingView.getContext()).inflate(R.layout.message, null);
        if (!msg.isEmpty()) {
            TextView textView = (TextView) layout.findViewById(R.id.message_info);
            textView.setText(msg);
        }
        showLayout(layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }

    protected void restore() {
        showLayout(loadingView);
    }

    private void showLayout(View layout) {
        if (parentView == null) {
            params = loadingView.getLayoutParams();
            if (loadingView.getParent() != null) {
                parentView = (ViewGroup) loadingView.getParent();
            } else {
                parentView = (ViewGroup) loadingView.getRootView().findViewById(android.R.id.content);
            }
            int count = parentView.getChildCount();
            for (int index = 0; index < count; index++) {
                if (loadingView == parentView.getChildAt(index)) {
                    viewIndex = index;
                    break;
                }
            }
            currentView = loadingView;
        }
        this.currentView = layout;
        // 如果已经是那个view，那就不需要再进行替换操作了
        if (parentView.getChildAt(viewIndex) != layout) {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
            parentView.removeViewAt(viewIndex);
            parentView.addView(layout, viewIndex, params);
        }
    }

}
