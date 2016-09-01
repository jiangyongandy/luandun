package com.jy.xinlangweibo.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.afollestad.materialdialogs.MaterialDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jy.xinlangweibo.R;
import com.jy.xinlangweibo.presenter.StatusPresenter;
import com.jy.xinlangweibo.ui.IView.HomeFragmentView;
import com.jy.xinlangweibo.ui.activity.MainActivity;
import com.jy.xinlangweibo.ui.activity.StatusDetailsActivity;
import com.jy.xinlangweibo.ui.activity.base.BaseActivity;
import com.jy.xinlangweibo.ui.adapter.StatusesAdapter;
import com.jy.xinlangweibo.ui.fragment.base.BaseCacheFragment;
import com.jy.xinlangweibo.utils.Logger;
import com.jy.xinlangweibo.utils.Utils;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends BaseCacheFragment implements View.OnClickListener,HomeFragmentView {
    @BindView(R.id.lv_status)
    PullToRefreshListView lvStatus;
    private View view;
    private View footView;
    private View tvLoad;
    private ImageView ivLoad;
    private ArrayList<Status> statusList = new ArrayList<Status>();
    private StatusesAdapter adapter;
    private int curPage;
    private PopupWindow pw;
    private MaterialDialog materialDialog;
    private StatusPresenter statusPresenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.fragment_home, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden) {
            ((MainActivity)activity).getToolbar().setVisibility(View.VISIBLE);
            ((MainActivity)activity).getNavTitle().setText("首页");
            ((MainActivity)activity).getNavRightIv().setOnClickListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mCache.clear();
//		缓存数据（IO操作，新开线程）耗时操作
        mCache.put("STATUES", statusList);
    }

    private void initView() {
        presenter = new StatusPresenter(activity,this);
        statusPresenter = (StatusPresenter)presenter;
        initPlv();
        initPop();
        showProgressDialog();
        statusPresenter.getHomeTimeline(1);
    }

    @Override
    public void showProgressDialog() {
        materialDialog = new MaterialDialog.Builder(activity)
                .content(R.string.please_wait)
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();
    }

    @Override
    public void dimissProgressDialog() {
        materialDialog.dismiss();
    }

    /**
     *
     */
    private void initPop() {
        View popView = LayoutInflater.from(activity).inflate(
                R.layout.pop_mainact_navright, null);
        pw = new PopupWindow(popView, Utils.dip2px(activity, 115),
                LinearLayout.LayoutParams.WRAP_CONTENT,true);
        // 设置允许在外点击消失
        pw.setOutsideTouchable(true);
        // 刷新状态
        pw.update();
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        pw.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.conversation_options_bg));
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ((MainActivity)activity).getMainmenu().getForeground().setAlpha( 0); // restore
            }
        });
    }


    /**
     *
     */
    private void initPlv() {
        footView = View.inflate(activity, R.layout.status_footview_loading,
                null);
        ivLoad = (ImageView) footView.findViewById(R.id.iv_status_loading);
        tvLoad = footView.findViewById(R.id.tv_status_loading);

        adapter = new StatusesAdapter(statusList);
        lvStatus.setAdapter(adapter);
        lvStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //前往微博详情界面
                Intent intent = new Intent(parent.getContext(), StatusDetailsActivity.class);
                //这里的position是从1开始
                intent.putExtra("Status", statusList.get(position-1));
                parent.getContext().startActivity(intent);
            }
        });
        lvStatus.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                statusPresenter.getHomeTimeline(1);
            }

            private void loadCaheData() {

            }
        });
    }

    @Override
    public void onGetTimeLineDone() {
        if (footView.getAnimation() != null) {
            footView.getAnimation().cancel();
        }
        ivLoad.setVisibility(View.GONE);
        lvStatus.onRefreshComplete();
    }

    @Override
    public void onExecptionComplete() {
        System.out.println("请求缓存");
        ArrayList<Status> statuses = (ArrayList<Status>) mCache
                .getAsObject("STATUES");
        if (statuses != null) {
            for (Status sta : statuses) {
                statusList.add(sta);
                if (statusList.size() == 50)
                    break;
            }
            adapter.notifyDataSetChanged();
        }
        tvLoad.setVisibility(View.GONE);
    }

    @Override
    public void updateHomeTimelineList(final int page, String response) {
        if (page == 1) {
            statusList.clear();
            curPage = 1;
        }
        curPage = page;
        StatusList list = StatusList.parse(response);
        if(list == null){
            Logger.showLog("list null","updatehometimeline faliure");
            return;
        }
        Logger.showLog("list.total_number----"+list.total_number+"list.statusList.size()---------"+list.statusList.size(),"statuslist");
        if (null != list.statusList) {
            for (Status sta : list.statusList) {
                statusList.add(sta);
            }
            adapter.notifyDataSetChanged();
            ListView refreshableView = lvStatus.getRefreshableView();
            if (list.statusList.size() < list.total_number) {
                addFootView(refreshableView);
            } else {
                removeFootView(refreshableView);
            }
        } else {
            ((BaseActivity) getActivity()).showToast(activity.getString(R.string.CONTINUE_REFRASH));
        }
        tvLoad.setVisibility(View.VISIBLE);
    }

    private void removeFootView(ListView refreshableView) {
        if (refreshableView.getFooterViewsCount() > 1) {
            refreshableView.removeFooterView(footView);
        }
    }

    private void addFootView(ListView refreshableView) {
        if (refreshableView.getFooterViewsCount() == 1) {
            refreshableView.addFooterView(footView);
            footView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    // 开启Loading动画
                    tvLoad.setVisibility(View.GONE);
                    ivLoad.setVisibility(View.VISIBLE);
                    ivLoad.startAnimation(AnimationUtils.loadAnimation(
                            activity, R.anim.ra_loading));
                    statusPresenter.getHomeTimeline(curPage + 1);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.nav_right_iv:
                showPopupWindow();
        }
    }

    public void showPopupWindow() {
        if (!pw.isShowing()) {
//            Logger.showLog("屏幕宽度：" + Utils.getDisplayWidthPixels(activity) + "弹窗宽度:" + View.MeasureSpec.getSize(pw.getWidth()), "计算弹窗偏移量");
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            pw.showAsDropDown(((MainActivity)activity).getToolbar(), Utils.getDisplayWidthPixels(activity) - View.MeasureSpec.getSize(pw.getWidth()) - Utils.dip2px(activity, 10), 0);
            ((MainActivity)activity).getMainmenu().getForeground().setAlpha(50); // dim
        } else {
            pw.dismiss();
        }
    }
}
