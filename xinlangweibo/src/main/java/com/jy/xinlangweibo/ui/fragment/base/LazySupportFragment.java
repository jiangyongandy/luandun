package com.jy.xinlangweibo.ui.fragment.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jy.xinlangweibo.R;

/**
 * Created by JIANG on 2016/10/27.
 */
public abstract class LazySupportFragment extends BaseSupportFragment  {

    // 标志位 标志已经初始化完成。
    protected boolean isPrepared;

    //标志位 fragment是否可见
    protected boolean isVisible;
    private View loadingView;
    private ViewGroup parentView;
    private int viewIndex;
    private ViewGroup.LayoutParams params;
    private View currentView;


    @Override
    protected void initViewAndEvent(View rootView) {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        finishCreateView(savedInstanceState);
        this.loadingView = getLoadingTargetView();
        isPrepared = true;
    }

    public void finishCreateView(Bundle state){};

    /**
     * Fragment数据的懒加载 此方法在viewpage滑动时有效
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint())
        {
            isVisible = true;
            onVisible();
        } else
        {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 此方法在hide方法调用生效
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    protected void onVisible()
    {
        lazyLoad();
    }

    protected void lazyLoad() {

    }

    protected void onInvisible() {}

    protected void loadData() {}

    protected void initRecyclerView() {}

    protected void initRefreshLayout() {}

    protected void updateUi() {}

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
