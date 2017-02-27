package com.jy.xinlangweibo.ui.fragment.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.utils.ToastUtils;

/**
 * Created by JIANG on 2016/12/6.
 */

public abstract class LazyFragment extends BaseFragment {

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

    protected void finishCreateView(Bundle state){};

    /**
     * Fragment数据的懒加载
     *
     * @param isVisibleToUser
     */
    //todo 此方法的判断在某些情况下并不准确，fragment第一次加载不会调用此方法，仅在viewpage切换时有用
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {

        super.setUserVisibleHint(isVisibleToUser);
//        if (getUserVisibleHint())
//        {
//            isVisible = true;
//            onVisible();
//        } else
//        {
//            isVisible = false;
//            onInvisible();
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ToastUtils.clearToast();
    }

    protected void onVisible()
    {
        lazyLoad();
    }

    protected void onInvisible() {}

    protected abstract View getLoadingTargetView();

    protected void lazyLoad() {

    }

    protected void loadData() {}

    protected void loadMore() {}

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

    protected void initRecyclerView() {}

    protected void initRefreshLayout() {}

    protected void updateUi() {}

    protected void showToast(CharSequence msg) {
        ToastUtils.show(getActivity(),msg, Toast.LENGTH_SHORT);
    }

    private void showLayout(View layout) {
        if (parentView == null) {
            params = loadingView.getLayoutParams();
            if (loadingView.getParent() != null) {
                parentView = (ViewGroup) loadingView.getParent();
            } else {
                //android.R.id.content is frameLayout
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
