package com.jy.xinlangweibo.ui.fragment.base;

import android.os.Bundle;
import android.view.View;

/**
 * Created by JIANG on 2016/10/27.
 */
public class LazySupportFragment extends BaseSupportFragment  {

    // 标志位 标志已经初始化完成。
    protected boolean isPrepared;

    //标志位 fragment是否可见
    protected boolean isVisible;


    @Override
    protected void initViewAndEvent(View rootView) {
        isPrepared = true;
        lazyLoad();
    }

    public void finishCreateView(Bundle state){};

    /**
     * Fragment数据的懒加载
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

    protected void onVisible()
    {

        lazyLoad();
    }

    protected void lazyLoad() {

    }

    protected void onInvisible() {}

    protected void loadData() {}

    protected void showProgressBar() {}

    protected void hideProgressBar() {}

    protected void initRecyclerView() {}

    protected void initRefreshLayout() {}

    protected void updateUi() {}

}
