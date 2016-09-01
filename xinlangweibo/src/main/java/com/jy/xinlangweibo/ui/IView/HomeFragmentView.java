package com.jy.xinlangweibo.ui.IView;

/**
 * Created by JIANG on 2016/8/27.
 */
public interface HomeFragmentView extends BaseIView {

    void showProgressDialog();

    void dimissProgressDialog();

    void updateHomeTimelineList(final int page, String response);

    void onGetTimeLineDone();

    void onExecptionComplete();

}
