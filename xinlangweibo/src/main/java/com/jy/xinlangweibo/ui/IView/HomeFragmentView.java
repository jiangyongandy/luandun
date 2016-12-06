package com.jy.xinlangweibo.ui.IView;

import com.jy.xinlangweibo.models.net.sinaapi.sinabean.StatusListBean;

/**
 * Created by JIANG on 2016/8/27.
 */
public interface HomeFragmentView extends BaseIView {

    void showProgressDialog();

    void dimissProgressDialog();

    void updateHomeTimelineList(final int page, StatusListBean statusList);

    void onGetTimeLineDone();

    void onExecptionComplete();

}
